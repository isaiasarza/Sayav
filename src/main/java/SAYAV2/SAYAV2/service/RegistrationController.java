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

public class RegistrationController {

	public static Route servicioPaginaRegistrar = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, PathUtil.Template.REGISTRATION);
    };
public static Route registrarNuevoUsuario = (Request request, Response response) -> {
	System.out.println("Registrar nuevo usuario\n");
	Map<String, Object> model = new HashMap<>();

	Usuario usuario;
	
	if(!isContraseñaValida(request)){
		System.out.println("Contraseña invalida");

		model.put("registrationFailed", true);
		return ViewUtil.render(request, model, PathUtil.Template.REGISTRATION);
	}

	usuario = initUsuario(request);
	String status = UsuarioController.registrarUsuario(usuario);
	model.put(status, true);
	return ViewUtil.render(request, model, PathUtil.Template.REGISTRATION);
};



public static boolean isContraseñaValida(Request request){
	String contraseña1,contraseña2;
	contraseña1 = RequestUtil.getQueryPassword(request);
	contraseña2 = RequestUtil.getQueryRepeatPassword(request);
	System.out.println(contraseña1 + " " + contraseña2);
	return contraseña1.equals(contraseña2);
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
