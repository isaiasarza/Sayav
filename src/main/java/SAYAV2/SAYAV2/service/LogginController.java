package SAYAV2.SAYAV2.service;

import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogginController {
	
	 public static Route serveLoginPage = (Request request, Response response) -> {
	        Map<String, Object> model = new HashMap<>();
//	        model.put("loggedOut", RequestUtil.removeSessionAttrLoggedOut(request));
//	        model.put("loginRedirect", RequestUtil.removeSessionAttrLoginRedirect(request));
	        return ViewUtil.render(request, model, PathUtil.Template.LOGIN);
	    };
//
	public static Route handleLoginPost = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();
		if (!UsuarioController.authenticate(RequestUtil.getQueryEmail(request),
				RequestUtil.getQueryPassword(request), RequestUtil.getQueryName(request), RequestUtil.getQueryLastName(request))) {
			model.put("authenticationFailed", true);
			return ViewUtil.render(request, model, PathUtil.Template.LOGIN);
		}
//		model.put("authenticationSucceeded", true);
//		request.session().attribute("currentUser", RequestUtil.getQueryEmail(request));
//		if (RequestUtil.getQueryLoginRedirect(request) != null) {
//			response.redirect(RequestUtil.getQueryLoginRedirect(request));
//		}
		return ViewUtil.render(request, model, PathUtil.Template.LOGIN);
	};

	 public static Route servicioPaginaRegistrar = (Request request, Response response) -> {
	        Map<String, Object> model = new HashMap<>();
//	        model.put("registrationSucceeded",false);
//	        model.put("registrationRedirect", RequestUtil.removeSessionAttrRegisterRedirect(request));
	        return ViewUtil.render(request, model, PathUtil.Template.REGISTRATION);
	    };
	public static Route registrarNuevoUsuario = (Request request, Response response) -> {
		System.out.println("Registrar nuevo usuario\n");
		Map<String, Object> model = new HashMap<>();
	
		Usuario usuario;
		
		if(!isContraseñaValida(request)){
			model.put("registrationFailed", true);
			return ViewUtil.render(request, model, PathUtil.Template.REGISTRATION);
		}
//		if((usuario = initUsuario(request)) == null){
//			model.put("registrationFailed", true);
//			return ViewUtil.render(request, model, PathUtil.Template.REGISTRATION);
//		}
		usuario = initUsuario(request);
		String status = UsuarioController.registrarUsuario(usuario);
		model.put(status, true);
		return ViewUtil.render(request, model, PathUtil.Template.REGISTRATION);
	};
	
	
	
	private static boolean isContraseñaValida(Request request){
		String contraseña1,contraseña2;
		contraseña1 = RequestUtil.getQueryPassword(request);
		contraseña2 = RequestUtil.getQueryRepeatPassword(request);
		return contraseña1 == contraseña2;
	}
	private static Usuario initUsuario(Request request){
	
		System.out.println("Inicializando Usuario");
		Usuario usuario = new Usuario();
		usuario.setNombre(RequestUtil.getQueryName(request));
		usuario.setApellido(RequestUtil.getQueryLastName(request));
		usuario.setContraseña(RequestUtil.getQueryPassword(request));
		usuario.setDireccion(RequestUtil.getQueryAddress(request));
		usuario.setEmail(RequestUtil.getQueryEmail(request));
		usuario.setTelefono(RequestUtil.getQueryPhoneNumber(request));
		usuario.setSubdominio(RequestUtil.getQuerySubdom(request));
		return usuario;
	}
}
