package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import javax.xml.bind.JAXBException;

import org.apache.commons.validator.routines.DomainValidator;

import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.FileUtils;
import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.MensajePendienteDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.dao.VotacionesDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.notificacion.GruposImpl;
import SAYAV2.SAYAV2.notificacion.Votacion;
import SAYAV2.SAYAV2.notificacion.Votaciones;
import spark.Request;
import spark.Response;
import spark.Route;

public class GroupController {

	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static GruposImpl grupos = new GruposImpl();
	private static File mensajesFile = new File(FileUtils.getMensajesFile());
	private static VotacionesDao votacionesDao = VotacionesDao.getInstance();
	private static MensajePendienteDao mensajesDao = MensajePendienteDao.getInstance();

	public static Route getNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar();
		System.out.println(usuario);

		Mensaje mensaje = new Mensaje();
		model.put("mensaje", mensaje);

		model.put("user", usuario);
		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);

	};

	public static Route postNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		Usuario usuario = usuarioDao.cargar();
		String nombre = RequestUtil.getNewGroupName(request);

		Grupo nuevoGrupo = new Grupo(nombre);

		if (nombre.contains("/")) {
			model.put("invalidUsername", true);
			model.put("user", usuario);
			return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
		}
		if (usuario.addGrupo(nuevoGrupo)) {
			model.put("addGroupSucceeded", true);
			model.put("user", usuario);
			usuarioDao.guardar(usuario);
		} else {
			model.put("user", usuario);
			model.put("existingGroup", true);
		}

		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
	};

	public static Route getAllGroups = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar();
		model.put("user", usuario);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_GROUPS);

	};

	public static Route leaveGroup = (Request request, Response response) -> {
		Thread thread;

		LoginController.ensureUserIsLoggedIn(request, response);
		Usuario usuario = usuarioDao.cargar();
		String groupName = request.params("groupName");
		Grupo grupo = usuario.getSingleGrupoByName(groupName);
		if (!grupos.isInit()) {
			grupos.init();
		}

		if (!grupo.getPeers().isEmpty()) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						grupos.abandonarGrupo(grupo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, "Abandonando Grupo " + grupo.toString());
			thread.start();
			grupos.abandonarGrupo(grupo);
		}
		usuarioDao.eliminarGrupo(grupo);
		request.session().removeAttribute("user");
		request.session().attribute("user", usuario);
		request.session().attribute("leaveGroupSucceeded", true);
		response.redirect(PathUtil.Web.NEW_GROUP);
		return null;
	};
	public static Route getNewGroupMember = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar();
		String groupName = request.params("groupName");

		model.put("user", usuario);
		model.put("groupName", groupName);

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);

	};

	public static Route postNewGroupMember = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Thread thread;
		System.out.println("Agregando Miembro");
		String memberDomain, groupName;
		int puerto;
		Usuario usuario = usuarioDao.cargar();
		groupName = request.params("groupName");

		Grupo grupo = usuario.getSingleGrupoByName(groupName);
		DomainValidator domainValidator = DomainValidator.getInstance(false);
		memberDomain = RequestUtil.getQueryMemberDomain(request);
		puerto = RequestUtil.getQueryPuerto(request);		
		System.out.println("Miembro: " + memberDomain + ":" + puerto);
		/*if (memberDomain.contains("/")) {
			model.put("invalidDomain", true);
			model.put("user", usuario);
			model.put("group", grupo);
			model.put("groupName", groupName);
			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}*/
		
		if (puerto<0 || puerto ==0) {
			model.put("invalidPuerto", true);
			model.put("user", usuario);
			model.put("group", grupo);
			model.put("groupName", groupName);
			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}

//		if (!domainValidator.isValid(memberDomain)) {
//			model.put("invalidDomain", true);
//			model.put("user", usuario);
//			model.put("group", grupo);
//			model.put("groupName", groupName);
//			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
//		}

		model.put("groupName", groupName);
		model.put("group", grupo);
		model.put("user", usuario);

		if (memberDomain.equals(usuario.getSubdominio()) || memberDomain.equals("localhost:")) {
			System.out.println("El miembro es el mismo usuario");
			model.put("memberIsUser", true);
			model.put("user", usuario);

			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}

		Peer peer = new Peer(memberDomain,puerto);
		if (grupo.getPeers().contains(peer)) {
			model.put("existingMember", true);
			model.put("user", usuario);

			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}

		if (!grupos.isInit()) {
			grupos.init();
		}
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					grupos.añadirMiembro(grupo, peer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "Añadiendo Miembro: " + peer + " a grupo"  + grupo );
		thread.start();
		model.put("procesandoMiembro", true);
		model.put("user", usuario);
		model.put("grupo", grupo);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
//		if (!grupos.añadirMiembro(grupo, peer)) {
//			model.put("procesandoMiembro", true);
//			model.put("user", usuario);
//			model.put("group", grupo);
//			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
//
//		}
//
//		RequestUtil.removeSessionAttrUser(request);
//		model.put("addMemberSucceeded", true);
//		model.put("user", usuario);
//		model.put("group", grupo);
//		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);

	};

	public static Route getViewMembers = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("View Members");
		Usuario usuario = usuarioDao.cargar();
		String groupName = request.params("groupName");
		Grupo group = usuario.getSingleGrupoByName(groupName);

		model.put("groupName", groupName);
		model.put("group", group);
		model.put("user", usuario);

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
	};

	public static Route solicitarBaja = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Thread thread;
		if (!grupos.isInit()) {
			grupos.init();
		}

		String memberDomain, groupName;
		Usuario usuario = usuarioDao.cargar();
		groupName = request.params("groupName");
		Grupo grupo = usuario.getSingleGrupoByName(groupName);

		memberDomain = request.params("miembro");
		Peer miembro = usuarioDao.getPeer(memberDomain, grupo);

		if (!votacionesDao.exist(grupo.getId(), miembro, FileUtils.VOTACIONES_FILE)) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						grupos.solicitarBajaMiembro(grupo, miembro);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, "Solicitando baja del miembro " + miembro + " en el grupo " + grupo );
			thread.start();
			model.put("solicitar", true);
		} else {
			model.put("solicitudExistente", true);
		}

		model.put("user", usuario);
		model.put("groupName", grupo.getNombre());
		model.put("group", grupo);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
	};

	public static Route getVotaciones = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("Ver Votaciones");
		Usuario usuario = usuarioDao.cargar();
		Votaciones votacionesPendientes;
		Votaciones votaciones;

		votacionesPendientes = votacionesDao.cargar(FileUtils.VOTACIONES_PENDIENTES_FILE);
		votaciones = votacionesDao.cargar(FileUtils.VOTACIONES_FILE);
		System.out.println("PUTO ERROR" +votaciones);
		model.put("user", usuario);
		model.put("currentUser", true);
		model.put("solicitudes", votaciones);
		model.put("votaciones", votacionesPendientes);

		return ViewUtil.render(request, model, PathUtil.Template.VER_VOTACIONES);
	};

	@SuppressWarnings("unused")
	private static Votaciones set(File file) throws JAXBException {
		if (file.exists()) {
			return votacionesDao.cargar(file);
		}
		return new Votaciones();
	}

	public static Route votarBaja = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		System.out.println("Ver Votaciones");
		Votaciones votacionesPendientes;
		Thread thread;
		String voto = request.params("voto");

		String votacionId = request.params("id");

		Votacion votacion = votacionesDao.getVotacion(votacionId, FileUtils.VOTACIONES_PENDIENTES_FILE);

		if (!grupos.isInit()) {
			grupos.init();
		}

		if (voto.equals("aceptar")) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						grupos.aceptarBajaMiembro(votacion);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, "Voto positivo");
			thread.start();
		} else {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						grupos.rechazarBajaMiembro(votacion);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, "Voto negativo");
			thread.start();
		}
		votacionesPendientes = votacionesDao.eliminarVotacion(votacion, FileUtils.VOTACIONES_PENDIENTES_FILE);
		System.out.println(votacionesPendientes);
		response.redirect(PathUtil.Web.VER_VOTACIONES);
		return null;
//		votacionesPendientes = votacionesDao.cargar(FileUtils.VOTACIONES_PENDIENTES_FILE);
//		votaciones = votacionesDao.cargar(FileUtils.VOTACIONES_FILE);
//
//		model.put("user", usuario);
//		model.put("currentUser", true);
//		model.put("solicitudes", votaciones);
//		model.put("votaciones", votacionesPendientes);
//		model.put("votacionExitosa", true);
//
//		return ViewUtil.render(request, model, PathUtil.Template.VER_VOTACIONES);
		
	};

	public static Route getAllMenssages = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar();
		MensajesPendientes mp;
		String estado = request.session().attribute("status");
		System.out.println(estado);
		if (estado == null || estado.isEmpty()) {
			estado = EstadoUtils.Estado.PENDIENTE;
		}
		mensajesDao.setFile(mensajesFile);
		mp = mensajesDao.cargar();

		Predicate<Mensaje> mensajePredicate = m -> !m.getEstado().equals(EstadoUtils.Estado.PENDIENTE);
		mp.getMensaje().removeIf(mensajePredicate);
		Collections.sort(mp.getMensaje());
		Collections.reverse(mp.getMensaje());
		model.put("estados", EstadoUtils.Estado.class);
		model.put("user", usuario);
		model.put("mensajesPendientes", mp);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_MESSAGES);

	};

	public static Route getAllMessagesByStatus = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		Usuario usuario = usuarioDao.cargar();
		String estado = request.params("estado");

		MensajesPendientes mp = mensajesDao.cargar(mensajesFile);
		Predicate<Mensaje> mensajePredicate = m -> !m.getEstado().equals(estado);
		mp.getMensaje().removeIf(mensajePredicate);
		Collections.sort(mp.getMensaje());
		Collections.reverse(mp.getMensaje());

		model.put("estados", EstadoUtils.Estado.class);
		model.put("user", usuario);
		model.put("mensajesPendientes", mp);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_MESSAGES);

	};

	public static Route postCambiarEstado = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		Usuario usuario = usuarioDao.cargar();
		String estado = request.params("estado");
		String id = request.params("id");
		mensajesDao.setFile(mensajesFile);
		if(!mensajesDao.cambiarEstado(id,estado)){
			
		}
		MensajesPendientes mp = mensajesDao.cargar(mensajesFile);

		Predicate<Mensaje> mensajePredicate = m -> !m.getEstado().equals(estado);
		mp.getMensaje().removeIf(mensajePredicate);
		Collections.sort(mp.getMensaje());
		Collections.reverse(mp.getMensaje());

		model.put("estados", EstadoUtils.Estado.class);
		model.put("user", usuario);
		model.put("mensajesPendientes", mp);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_MESSAGES);

	};

	public static Route eliminarMensaje = (Request request, Response response) -> {

		Map<String, Object> model = new HashMap<>();
		System.out.println("Eliminar Mensaje");
		Usuario usuario = usuarioDao.cargar();

		String mensajeId = request.params("mensajeId");

		mensajesDao.setFile(mensajesFile);
		Mensaje eliminado = mensajesDao.eliminarMensajeId(mensajeId);
		if (eliminado == null) {
			model.put("eliminacionFallida", true);
			request.session().removeAttribute("mensajesPendientes");
		} else
			model.put("eliminacionExitosa", true);

		request.session().attribute("mensajesPendientes", mensajesDao.cargar(mensajesFile));
		request.session().attribute("estados", EstadoUtils.Estado.class);
		request.session().attribute("user", usuario);

		response.redirect(PathUtil.Web.VIEW_ALL_MESSAGES);

		return null;
	};

}
