package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.validator.routines.UrlValidator;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.bussines.ControllerMQTT;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.dao.VotacionesDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.Grupo;
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
	private static File file = new File("SAYAV");
	private static ControllerMQTT not = ControllerMQTT.getInstance();
	private static GruposImpl grupos = new GruposImpl();
	private static File votacionesPendientesFile = new File("votaciones_pendientes");
	private static	File votacionesFile = new File("votaciones");
	private static VotacionesDao votacionesDao = VotacionesDao.getInstance();

	public static Route getNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		//System.out.println("Nuevo Grupo");
		Usuario usuario = usuarioDao.cargar(file);
		System.out.println(usuario);

		Mensaje mensaje = new Mensaje();
//		mensaje.setTipo(TipoMensaje.ALERTA);
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
        
		Grupo nuevoGrupo = new Grupo(nombre);
		 
		if (usuario.addGrupo(nuevoGrupo)) {
			model.put("addGroupSucceeded", true);
			model.put("user", usuario);

			usuarioDao.guardar(usuario, file);
			String nuevoMiembro = nuevoGrupo.getId()+ "/" + TipoMensajeUtils.NUEVO_MIEMBRO;
			System.out.println(nuevoMiembro);

            not.receive(nuevoMiembro,2);
			

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
		Grupo grupo = usuario.getSingleGrupoByName(groupName);
		
		
		if(!grupo.getPeers().isEmpty()){
//			if(Notificacion.verConectividad(grupo.getPeers())){
//				model.put("leaveGroupFailed", true);			
//				return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
//			}
//
//			if(notificarAbandonoGrupos(groupName)){
//				usuario.removeGrupo(groupName);
//				usuarioDao.guardar(usuario, file);
//				
//				model.put("leaveGroupSucceeded", true);
//			}else{
//				
//				model.put("leaveGroupFailed", true);			
//			}

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

		Grupo grupo = usuario.getSingleGrupoByName(groupName);

		memberDomain = RequestUtil.getQueryMemberDomain(request);
		
		if(!memberDomain.contains("http://")){
			memberDomain = "http://" + memberDomain;
		}
		
		String[] schemes = {"http","https"};

		UrlValidator urlValidator = new UrlValidator(schemes);
		
		model.put("groupName", groupName);
		model.put("group", grupo);
		model.put("user", usuario);
		
		if(!urlValidator.isValid(memberDomain)){
			model.put("invalidDomain", true);
			model.put("user", usuario);

			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}
		if(memberDomain.equals(usuario.getSubdominio()) || memberDomain.equals("localhost:")){
			System.out.println("El miembro es el mismo usuario");
			model.put("memberIsUser", true);
			model.put("user", usuario);

			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}
		
		Peer peer = new Peer();
		peer.setDireccion(memberDomain);
		
		if(grupo.getPeers().contains(peer)){
			model.put("existingMember", true);
			model.put("user", usuario);

			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}

		if(!grupos.isInit()){
			grupos.init();
		}
		grupos.añadirMiembro(grupo, peer);
		
		RequestUtil.removeSessionAttrUser(request);
		model.put("addMemberSucceeded", true);
		model.put("user", usuario);
		model.put("group", grupo);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);

	};

	public static Route getViewMembers = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("View Members");
		Usuario usuario = usuarioDao.cargar(file);
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
		
		if(!grupos.isInit()){
			grupos.init();
		}

		String memberDomain, groupName;
		Usuario usuario = usuarioDao.cargar(file);
		groupName = request.params("groupName");
		Grupo grupo = usuario.getSingleGrupoByName(groupName);

		memberDomain = request.params("miembro");
		Peer miembro = usuarioDao.getPeer(memberDomain, grupo);
		
		if(!votacionesDao.exist(grupo,miembro,votacionesFile)){
			grupos.solicitarBajaMiembro(grupo, miembro);
			model.put("solicitar",true);
		}else{
			model.put("solicitudExistente",true);
		}
		

		model.put("user", usuario);
		model.put("groupName",grupo.getNombre());
		model.put("group", grupo);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
	};
	

	public static Route getVotaciones = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("Ver Votaciones");
		Usuario usuario = usuarioDao.cargar(file);
		Votaciones votacionesPendientes;
		Votaciones votaciones;
		
		
		votacionesPendientes = set(votacionesPendientesFile);
		votaciones = set(votacionesFile);
		
		model.put("user", usuario);
		model.put("currentUser", true);
		model.put("solicitudes",votaciones);
		model.put("votaciones", votacionesPendientes);

		return ViewUtil.render(request, model, PathUtil.Template.VER_VOTACIONES);
	};

	private static Votaciones set(File file) throws JAXBException{
		if(file.exists()){
			return votacionesDao.cargar(file);
		}
		return new Votaciones();
	}
	
	public static Route votarBaja = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("Ver Votaciones");
		Usuario usuario = usuarioDao.cargar(file);
		Votaciones votacionesPendientes;
		Votaciones votaciones;
		
		String voto = request.params("voto");
		
		String votacionId = request.params("id");
		
		Votacion votacion = votacionesDao.getVotacion(votacionId, votacionesPendientesFile);
		
		if(!grupos.isInit()){
			grupos.init();
		}
		
		if(voto.equals("aceptar")){
			grupos.aceptarBajaMiembro(votacion);
		}else{
			grupos.rechazarBajaMiembro(votacion);
		}
		
		votacionesPendientes = set(votacionesPendientesFile);
		votaciones = set(votacionesFile);
		
		model.put("user", usuario);
		model.put("currentUser", true);
		model.put("solicitudes",votaciones);
		model.put("votaciones", votacionesPendientes);

		return ViewUtil.render(request, model, PathUtil.Template.VER_VOTACIONES);
	};
	

}
