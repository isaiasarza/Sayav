package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;


import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.TipoMensaje;
import SAYAV2.SAYAV2.Utils.ViewUtil;
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

	public static Route getNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("Nuevo Grupo");
		Usuario usuario = usuarioDao.cargar(file);
		System.out.println(usuario);
		model.put("user", usuario);
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
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_GROUPS);

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

		notificarNuevoMiembro(grupo, memberDomain, usuario);
		model.put("groupName", groupName);
		model.put("group", grupo);
		model.put("user", usuario);

		// grupo.setNombre(memberDomain);
		if (!grupo.addPeer(memberDomain)) {
			
			model.put("existingMember", true);
			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}

		usuarioDao.guardar(usuario, file);
		RequestUtil.removeSessionAttrUser(request);
	
		model.put("addMemberSucceeded", true);
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
		 return ViewUtil.render(request, model,
		 PathUtil.Template.VIEW_GROUP_MEMBER);
	};

	/**
	 * Se avisara al resto del grupo que hay un nuevo miembro
	 * 
	 * @param grupo
	 * @param memberDomain
	 */
	private static void notificarNuevoMiembro(Grupo grupo, String memberDomain, Usuario usuario) {
		try {
			notificarGrupos(grupo, memberDomain,TipoMensaje.NUEVO_MIEMBRO);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Avisa a todos los miembros del grupo del nuevo miembro 
	 * @param grupo
	 * @param memberDomain
	 * @param tipo
	 * @throws JAXBException
	 */
	private static void notificarGrupos(Grupo grupo, String memberDomain, String tipo) throws JAXBException {
		
		Mensaje mensaje = new Mensaje();
		Usuario usuario = usuarioDao.cargar(file);
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setTipo(tipo);
		mensaje.setDescripcion("El miembro " + memberDomain + " se ha unido al grupo");
		mensaje.setDatos(memberDomain);
		
		for(Peer p: grupo.getPeers()){
			PostGrupo.post(p.getDireccion(), mensaje);
		}
	}
}
