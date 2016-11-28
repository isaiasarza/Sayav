package SAYAV2.SAYAV2.service;

import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.model.Sector;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginController {
	
	/**
	 * Muestra el formulario de login
	 */
	public static Route serveLoginPage = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();
		model.put("loggedOut", RequestUtil.removeSessionAttrLoggedOut(request));
		model.put("loginRedirect", RequestUtil.removeSessionAttrLoginRedirect(request));
		
		return ViewUtil.render(request, model, PathUtil.Template.LOGIN);
	};

	/**
	 * Valida los datos y muestra el menú de usuario ó envía mensaje de error.
	 */
	public static Route handleLoginPost = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();
		System.out.println("Autentificando usuario");
		String status;
		Usuario usuario;
		
		status = UsuarioController.authenticate(RequestUtil.getQueryEmail(request), RequestUtil.getQueryPassword(request),
				model);
		model.put(status,true);
		if(!status.equals("authenticationSucceeded")){
			System.out.println("Incorrect data");
			return ViewUtil.render(request, model, PathUtil.Template.LOGIN);
		}

		request.session().attribute("currentUser",
				RequestUtil.getQueryName(request) + " " + RequestUtil.getQueryLastName(request));
		if (RequestUtil.getQueryLoginRedirect(request) != null) {
			response.redirect(RequestUtil.getQueryLoginRedirect(request));
		}
		usuario = UsuarioController.getCurrentUser();
		System.out.println(usuario);
		response.redirect(PathUtil.Web.MENU);
		return null;
	};
	/**
	 * Cierra la sesión y muestra la pantalla de login
	 */
	public static Route handleLogoutPost = (Request request, Response response) -> {
		request.session().removeAttribute("currentUser");
		request.session().attribute("loggedOut", true);
		response.redirect(PathUtil.Web.LOGIN);
		return null;
	};
	/**
	 * 
	 * @param request
	 * @param response
	 * Verifica que el usuario este loggeado
	 */
	public static void ensureUserIsLoggedIn(Request request, Response response) {
		if (request.session().attribute("currentUser") == null) {
			request.session().attribute("loginRedirect", request.pathInfo());
			response.redirect(PathUtil.Web.LOGIN);
		}
	};

}
