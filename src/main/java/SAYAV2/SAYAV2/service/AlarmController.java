package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class AlarmController {
	private static UsuarioDao usuarioDao;
	private static File file = new File("SAYAV");

	/**
	 * Habilita o deshabilita el alarma
	 */
	public static Route enableAlarmPost = (Request request, Response response) -> {
		usuarioDao = UsuarioDao.getInstance();
		System.out.println("Alarm change status");

		// request.session().removeAttribute("currentUser");
		// request.session().attribute("loggedOut", true);
		Map<String, Object> model = new HashMap<>();
		
		Usuario usuario;
//		usuario = UsuarioController.getCurrentUser();
		usuario = usuarioDao.cargar(file);
		boolean status = usuario.getAlarmaHabilitada();
		usuario.setAlarmaHabilitada(!status);	
		System.out.println("Us " + usuario);
		usuarioDao.guardar(usuario, file);
		
		model.put("user", usuario);
		Usuario u = request.session().attribute("user");
		System.out.println("Usuario " + u);
//		response.redirect(PathUtil.Web.MENU);
		return ViewUtil.render(request, model, PathUtil.Template.MENU);
//		return null;
	};
}
