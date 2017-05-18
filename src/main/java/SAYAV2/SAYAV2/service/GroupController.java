package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.routines.UrlValidator;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.TipoMensaje;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.bussines.ControllerMQTT;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Mensaje;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class GroupController {

	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static File file = new File("SAYAV");
	private static ControllerMQTT not = ControllerMQTT.getInstance();


	public static Route getNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		//System.out.println("Nuevo Grupo");
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
        
		Grupo nuevoGrupo = new Grupo(nombre);
		 
		if (usuario.addGrupo(nuevoGrupo)) {
			model.put("addGroupSucceeded", true);
			model.put("user", usuario);

			usuarioDao.guardar(usuario, file);
			String nuevoMiembro = nuevoGrupo.getId()+ "/" + TipoMensaje.NUEVO_MIEMBRO;
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

//		Notificar a los miembros que hay un nuevo miembro
		
		if(!grupo.getPeers().isEmpty()){
			not.notificarMiembros(peer,grupo);
		}
		
//		Notificar al nuevo miembro, que es parte de un grupo
		
		not.notificarNuevoGrupo(peer,grupo,usuario);
		
//TODO: 	Check que todo esta bien
		
//		Agregar al miembro al grupo
		
		grupo.addPeer(peer.getDireccion());

		usuarioDao.guardar(usuario, file);
		RequestUtil.removeSessionAttrUser(request);
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

	
	

}
