package SAYAV2.SAYAV2.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.TipoMensaje;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.MensajePendienteDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.GrupoPeer;
import SAYAV2.SAYAV2.model.Mensaje;
import SAYAV2.SAYAV2.model.MensajePendienteData;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class GroupController {

	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static File file = new File("SAYAV");
	private static MensajePendienteDao mensajeDao = MensajePendienteDao.getInstance();
	private static File filemensajes = new File("Mensajes");
	private static JsonTransformer jsonTransformer = new JsonTransformer();

	public static Route getNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("Nuevo Grupo");
		Usuario usuario = usuarioDao.cargar(file);
		System.out.println(usuario);

		Mensaje mensaje = new Mensaje();
		mensaje.setTipo(TipoMensaje.ALERTA);
		model.put("mensaje", mensaje);

		model.put("user", usuario);

		// Verifico el estado del response HTTP
		// if(response.raw().getStatus() ==
		// javax.ws.rs.core.Response.Status.OK.getStatusCode())
		// System.out.println("Ok");
		//
		// System.out.println(response.raw().getStatus());

		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);

	};

	public static Route postNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		Usuario usuario = usuarioDao.cargar(file);
		String nombre = RequestUtil.getNewGroupName(request);

		if (usuario.addGrupo(new Grupo(nombre))) {
			model.put("addGroupSucceeded", true);
			model.put("user", usuario);

			usuarioDao.guardar(usuario, file);

		} else {
			model.put("user", usuario);

			model.put("existingGroup", true);

		}

		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
	};

	public static Route getAllGroups = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar(file);

		model.put("user", usuario);

		System.out.println(response.raw().getStatus());

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_GROUPS);

	};

	public static Route leaveGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar(file);
		String groupName = request.params("groupName");

		if(notificarAbandonoGrupos(groupName)){
			usuario.removeGrupo(groupName);
			usuarioDao.guardar(usuario, file);
			model.put("leaveGroupSucceeded", true);
		}else{
			model.put("leaveGroupFailed", true);			
		}

		model.put("user", usuario);
		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);

	};
	public static Route getNewGroupMember = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar(file);
		String groupName = request.params("groupName");

		model.put("user", usuario);
		model.put("groupName", groupName);

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);

	};

	public static Route postNewGroupMember = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		System.out.println("Agregando Miembro");
		String memberDomain, groupName;
		Usuario usuario = usuarioDao.cargar(file);
		groupName = request.params("groupName");

		Grupo grupo = usuario.getSingleGrupo(groupName);

		memberDomain = RequestUtil.getQueryMemberDomain(request);
		
		if(memberDomain.equals(usuario.getSubdominio()) || memberDomain.equals("localhost:29080")){
			System.out.println("El miembro es el mismo usuario");
			model.put("memberIsUser", true);
		}

		if(notificarNuevoMiembro(grupo, memberDomain, usuario)){
			System.out.println();
			if (!grupo.addPeer(memberDomain)) {
				System.out.println("Miembro existente");
				model.put("existingMember", true);
				//return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
			}else{
				System.out.println("Miembro agregado");
				model.put("addMemberSucceeded", true);
			}
		}else{
			System.out.println("Fallo, problemas con conexiones");
			model.put("addMemberFailed", true);
		}
//		if (notificarMiembro(grupo, memberDomain)) {
//
//		}
//		;
//		notificarGrupos(grupo, memberDomain, TipoMensaje.NUEVO_MIEMBRO);

		model.put("groupName", groupName);
		model.put("group", grupo);
		model.put("user", usuario);

	

		usuarioDao.guardar(usuario, file);
		RequestUtil.removeSessionAttrUser(request);
		model.put("user", usuario);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);

	};

	public static Route getViewMembers = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("View Members");
		Usuario usuario = usuarioDao.cargar(file);
		String groupName = request.params("groupName");
		Grupo group = usuario.getSingleGrupo(groupName);

		model.put("groupName", groupName);
		model.put("group", group);
		model.put("user", usuario);

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
	};

	/**
	 * Se avisara al resto del grupo que hay un nuevo miembro
	 * 
	 * @param grupo
	 * @param memberDomain
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws ProtocolException
	 * @throws JAXBException
	 */
	public static synchronized boolean notificarNuevoMiembro(Grupo grupo, String memberDomain, Usuario usuario)
			throws ProtocolException, MalformedURLException, IOException, JAXBException {

		if (notificarMiembro(grupo, memberDomain)) {
			if (notificarGrupos(grupo, memberDomain, TipoMensaje.NUEVO_MIEMBRO)) {
				return true;
			}
		}
		return false;

	}

	private static boolean notificarAbandonoGrupos(String groupName) {
		try {
			Usuario usuario = usuarioDao.cargar(file);
			Grupo grupo = usuario.getSingleGrupo(groupName);
			GrupoPeer g = new GrupoPeer();
			g.setPeer(usuario.getSubdominio());
			g.setGrupo(grupo.getNombre());
			Mensaje mensaje = new Mensaje();
			mensaje.setOrigen(usuario.getSubdominio());
			mensaje.setDescripcion("");
			mensaje.setTipo(TipoMensaje.BAJA_MIEMBRO);
			mensaje.setDatos(jsonTransformer.render(g));
			
			for(Peer p: grupo.getPeers()){
				try {
					PostGrupo.post(p.getDireccion(), mensaje);
				}catch (IOException e) {
					// TODO Auto-generated catch block
					
					e.printStackTrace();
					return false;
				}
			}
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static void notificarAbandono1Grupos(String groupName) throws JAXBException, ProtocolException, MalformedURLException, IOException {
		Usuario usuario = usuarioDao.cargar(file);

		Grupo grupo = usuario.getSingleGrupo(groupName);
		GrupoPeer g = new GrupoPeer();
		g.setGrupo(groupName);
		g.setPeer(usuario.getSubdominio());
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setDescripcion("El miembro " + usuario.getSubdominio() + " ha dejado el grupo");
		mensaje.setTipo(TipoMensaje.BAJA_MIEMBRO);
		mensaje.setDatos(jsonTransformer.render(g));
		
		for(Peer p: grupo.getPeers()){
			PostGrupo.post("http://" + p.getDireccion() + PathUtil.Web.GRUOP_NOTIFICATION, mensaje);
		}

		
	}

public static boolean notificarMiembro(Grupo grupo, String memberDomain) throws JAXBException {
	
		Usuario usuario = usuarioDao.cargar(file);

		GrupoPeer g = new GrupoPeer();
		g.setGrupo(grupo.getNombre());
		for (Peer p : grupo.getPeers()) {
			g.getListaPeers().add(p.getDireccion());
		}
		g.getListaPeers().add(usuario.getSubdominio());
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setDescripcion("");
		mensaje.setTipo(TipoMensaje.NUEVO_GRUPO);
		mensaje.setDatos(jsonTransformer.render(g));

		try {
			PostGrupo.post("http://" + memberDomain + PathUtil.Web.GRUOP_NOTIFICATION, mensaje);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * Avisa a todos los miembros del grupo del nuevo miembro
	 * 
	 * @param grupo
	 * @param memberDomain
	 * @param tipo
	 * @return
	 * @throws JAXBException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws ProtocolException
	 */
	public static boolean notificarGrupos(Grupo grupo, String memberDomain, String tipo) throws JAXBException {

		Mensaje mensaje = new Mensaje();
		Usuario usuario = usuarioDao.cargar(file);
		GrupoPeer datos = new GrupoPeer();
		datos.setGrupo(grupo.getNombre());
		datos.setPeer(memberDomain);
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setTipo(tipo);
		mensaje.setDescripcion("El miembro " + memberDomain + " se ha unido al grupo");
		mensaje.setDatos(jsonTransformer.render(datos));

		System.out.println("Notificando Peers");
		for (Peer p : grupo.getPeers()) {
			System.out.println("Notificando Peer " + "http://" + p.getDireccion());
			try {
				PostGrupo.post(p.getDireccion() + PathUtil.Web.GRUOP_NOTIFICATION, mensaje);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		return true;
	}

}
