package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.bussines.Alarma;
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
		boolean status = usuario.isAlarmaHabilitada();
		usuario.setAlarmaHabilitada(!status);	
		System.out.println("Us " + usuario);
		usuarioDao.guardar(usuario, file);
		UsuarioController.setCurrentUser(usuario);

		model.put("user", usuario);
		Usuario u = request.session().attribute("user");
		System.out.println("Usuario " + u);
//		response.redirect(PathUtil.Web.MENU);
		return ViewUtil.render(request, model, PathUtil.Template.MENU);
//		return null;
	};
	
	public static Route panicButton = (Request request, Response response) -> {
		usuarioDao = UsuarioDao.getInstance();
		System.out.println("Panic Button");

		// request.session().removeAttribute("currentUser");
		// request.session().attribute("loggedOut", true);
		Map<String, Object> model = new HashMap<>();
		
		Usuario usuario;
		usuario = usuarioDao.cargar(file);
		String message = "El boton de panico ha sido activado en el domicilio " + usuario.getDireccion() + "\nEl dueño del domicilio es " + usuario.getNombre() + " " + usuario.getApellido();
		FirebaseCloudMessageController.post("Peligro!",message);
		Alarma.notificar(usuario);
		model.put("panicButton",true);
		model.put("user",usuario);

		return ViewUtil.render(request, model, PathUtil.Template.MENU);

	};
	
}
