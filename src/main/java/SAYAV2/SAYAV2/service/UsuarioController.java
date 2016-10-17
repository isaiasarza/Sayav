package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.mindrot.jbcrypt.BCrypt;

import SAYAV2.SAYAV2.Utils.PasswordUtils;
import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.service.LoginController;
import spark.Request;
import spark.Response;
import spark.Route;

public class UsuarioController {
	

	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static Usuario currentUser;
	private static String fileName = "SAYAV";
	private static File file = new File(fileName);
	
	public UsuarioController() {
		super();
	}
	/**
	 * 
	 * @param usuario
	 * @return the state of the registration
	 */
	public static String registrarUsuario(Usuario usuario) {
		usuarioDao = UsuarioDao.getInstance();
//		Usuario u = new Usuario();
		System.out.println("Registrando: " + usuario);
		file.setWritable(true);
		file.setReadable(true);
		try {
			usuarioDao.cargar(file);
		} catch (JAXBException e) {
			String hashedPassword = PasswordUtils.generarContraseña(usuario);
			usuario.setContraseña(hashedPassword);
			usuarioDao.guardar(usuario, file);
			System.out.println("Usuario guardado");
			return "registrationSucceeded";
		}
		return "existingUser";
	}


/**
 * 
 * @param queryEmail 
 * @param queryPassword
 * @param queryName
 * @param queryLastName
 * @param model 
 * @return
 */
	public static String authenticate(String queryEmail, String queryPassword, Map<String, Object> model) {
		usuarioDao = UsuarioDao.getInstance();
		System.out.println("Autentificando Usuario");
		System.out.println(file.toString());
		Usuario usuario;
		//leo usuario del archivo
		try {
			usuario = (Usuario) usuarioDao.cargar(file);
			//System.out.println(usuario);
			//genero contraseña hash
			
			if(usuario.getEmail().equals(queryEmail) && 
					PasswordUtils.esContraseñaValida(queryPassword, usuario.getContraseña())){
				currentUser=usuario;
				return "authenticationSucceeded";
			}
			if(!usuario.getEmail().equals(queryEmail) && 
					!PasswordUtils.esContraseñaValida(queryPassword, usuario.getContraseña())){
				return "authenticationFailed";
			}
			if(!usuario.getEmail().equals(queryEmail)){
				return "wrongEmail";
			}
			if(!PasswordUtils.esContraseñaValida(queryPassword, usuario.getContraseña())){
				return "wrongPassword";
			}
		
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return "authenticationFailed";
	}
	
	public static Route viewUserData = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		System.out.println("View User Data");
        Map<String, Object> model = new HashMap<>();
		model.put("menuRedirect", RequestUtil.removeSessionAttrMenuRedirect(request));
        model.put("user",currentUser);
        return ViewUtil.render(request, model, PathUtil.Template.MENU);
    };
    
    
    public static Route update = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
    	System.out.println("update");  	
    	Map<String, Object> model = new HashMap<>();

        return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);
    };
   
    
    public static Route showUpdate = (Request request, Response response) -> {
    	System.out.println("ShowUpdate");  
    	Map<String, Object> model = new HashMap<>();

		System.out.println(PathUtil.Web.MENU);
        return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);

    };
    
    public static Route updateUser = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
    	System.out.println("ShowUpdate");  
    	Map<String, Object> model = new HashMap<>();

		System.out.println(PathUtil.Web.MENU);
        return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);

    };
    
    public static Usuario getCurrentUser(){
    	return currentUser;
    }
    
    public static Usuario update(Request request) throws JAXBException{

    	Usuario usuario = (Usuario) usuarioDao.cargar( file);
        String nombre = RequestUtil.getQueryName(request);
        String apellido = RequestUtil.getQueryLastName(request);
        String domicilio = RequestUtil.getQueryAddress(request);
        String email = RequestUtil.getQueryEmail(request);
        String subdominio = RequestUtil.getQuerySubdom(request);
        String numeroTelefono = RequestUtil.getQueryPhoneNumber(request);
        String contraseña = RequestUtil.getQueryPassword(request);
        String contraseñaRepetida = RequestUtil.getQueryRepeatPassword(request);
        if(!nombre.isEmpty())
        	usuario.setNombre(nombre);
        if(!apellido.isEmpty())
        	usuario.setApellido(apellido);
        if(!domicilio.isEmpty())
        	usuario.setDireccion(domicilio);
        if(!email.isEmpty())
        	usuario.setEmail(email);
        if(!subdominio.isEmpty())
        	usuario.setSubdominio(subdominio);
        if(!numeroTelefono.isEmpty())
        	usuario.setTelefono(numeroTelefono);
        if(!contraseña.isEmpty() && contraseña.equals(contraseñaRepetida))
        	usuario.setContraseña(BCrypt.hashpw(contraseña, usuario.getSalt()));
    	return usuario;
    }
      
}
