package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Grupo;
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
		model.put("user",usuario);
		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
	
	};

	public static Route postNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		Usuario usuario = usuarioDao.cargar(file);
		String nombre = RequestUtil.getNewGroupName(request);
	
		if (usuario.addGrupo(new Grupo(nombre))) {
			model.put("addGroupSucceeded", true);
			usuarioDao.guardar(usuario, file);

		} else {
			System.out.println("Grupo existente");
			model.put("existingGroup", true);
		}
		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
	};

	public static Route getAllGroups = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_GROUPS);
	
	};
	
	public static Route getNewGroupMember= (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		String groupName = request.params("groupName");
		model.put("groupName",groupName);
		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP_MEMBER);
	
	};
	
	public static Route postNewGroupMember= (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		String memberDomain,groupName;
		Usuario usuario = usuarioDao.cargar(file);
		groupName = request.params("groupName");
	
		Grupo grupo = usuario.getSingleGrupo(groupName);
		
		memberDomain = RequestUtil.getQueryMemberDomain(request);
		notificarNuevoMiembro(grupo,memberDomain,usuario);
		
		grupo.addDominio(memberDomain);
		
		usuarioDao.guardar(usuario, file);
		RequestUtil.removeSessionAttrUser(request);
		model.put("user",usuario);
		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
	};
	
	public static Route getViewMembers = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("View Members");
		Usuario usuario = usuarioDao.cargar(file);
		String groupName = request.params("groupName");
		Grupo group = usuario.getSingleGrupo(groupName);
		model.put("group", group);
	
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
	
	};

	/**
	 * Se avisara al resto del grupo que hay un nuevo miembro
	 * @param grupo
	 * @param memberDomain
	 */
	private static void notificarNuevoMiembro(Grupo grupo, String memberDomain,Usuario usuario) {
		// TODO 
		notificarGrupos(grupo,memberDomain);
		notificarMiembro(grupo,memberDomain,usuario.getSubdominio());
	}

	/**
	 * Se avisa al nuevo miembro que pertenece a un nuevo grupo, y se envian los integrantes del grupo
	 * @param grupo
	 * @param memberDomain
	 * @param string 
	 */
	private static void notificarMiembro(Grupo grupo, String memberDomain, String currentUserDomain) {
		// TODO Auto-generated method stub
		
	}

	private static void notificarGrupos(Grupo grupo, String memberDomain) {
		// TODO Auto-generated method stub
		
	}
}
