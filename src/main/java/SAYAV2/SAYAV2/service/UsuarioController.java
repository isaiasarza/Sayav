package SAYAV2.SAYAV2.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.mindrot.jbcrypt.BCrypt;

import SAYAV2.SAYAV2.Utils.PasswordUtils;
import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Sector;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class UsuarioController {
	

	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static Usuario currentUser;
	//private static File file = new File(FileUtils.getUsuarioFile());
	
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
		System.out.println("Registrando: " + usuario);

		try {
			usuarioDao.cargar();
		} catch (JAXBException e) {
			String hashedPassword = PasswordUtils.generarContraseña(usuario);
			usuario.setContraseña(hashedPassword);
			try {
				usuarioDao.guardar(usuario);
			} catch (JAXBException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			currentUser = usuario;
			System.out.println("Usuario guardado");
			return "registrationSucceeded";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		Usuario usuario;
		//leo usuario del archivo
		try {
			usuario = (Usuario) usuarioDao.cargar();
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
    	Usuario usuario = usuarioDao.cargar();
    	model.put("user", usuario);
        return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);
    };
   
    
    public static Route showUpdate = (Request request, Response response) -> {
    	System.out.println("ShowUpdate");  
    	Map<String, Object> model = new HashMap<>();

    	Sector sector = new Sector();
        
		model.put("sector", sector);
    	model.put("user", currentUser);
		System.out.println(PathUtil.Web.MENU);
        return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);

    };
    
    public static Route viewUpdateUser = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
    	System.out.println("viewUpdateUser");  
    	Map<String, Object> model = new HashMap<>();   	
    	completarFormulario(model,request);
    	model.put("user", currentUser);
        return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);

    };
    public static Route updateUser = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
    	System.out.println("updateUser");  
    	Map<String, Object> model = new HashMap<>();   	
    	Usuario usuario;
    	try{
    		if((usuario = update(request))== null){
        		model.put("wrongPassword", true);
        		model.put("user",usuario);
        		return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);
    		}
    		usuarioDao.guardar(usuario);
    		System.out.println(usuario);
//    		request.session().attribute("updateSucceeded",true);
    		model.put("user", usuario);
    	}catch(JAXBException e){
    		e.printStackTrace();
        	model.put("updateFailed", true);
        	System.out.println("Update Failed");

//        	request.session().attribute("updateFailed",true);
        	return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);
    	}
    	
//        return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);
    	System.out.println("Update Succeeded");
    	
      
    	return ViewUtil.render(request, model, PathUtil.Template.UPDATE_USER);

    };
    
    public static Usuario getCurrentUser(){
    	return currentUser;
    }
    
    public static void setCurrentUser(Usuario currentUser) {
		UsuarioController.currentUser = currentUser;
	}
    
  
	private static void completarFormulario(Map<String, Object> model, Request request) {
		model.put("name",currentUser.getNombre());
		model.put("lastname", currentUser.getApellido());
	}
	public static Usuario update(Request request) throws JAXBException, IOException{

    	Usuario usuario = (Usuario) usuarioDao.cargar();
        String nombre = RequestUtil.getQueryName(request);
        String apellido = RequestUtil.getQueryLastName(request);
        String domicilio = RequestUtil.getQueryAddress(request);
        String email = RequestUtil.getQueryEmail(request);
        String subdominio = RequestUtil.getQuerySubdom(request);
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
        if(!contraseña.isEmpty()){
        	if(contraseñaRepetida.isEmpty() || contraseñaRepetida == null || contraseñaRepetida == "")
        		return null;
        	if(!contraseña.equals(contraseñaRepetida))
        		return null;
        	usuario.setContraseña(BCrypt.hashpw(contraseña, usuario.getSalt()));
        	
        }
    	return usuario;
    }
      
}
