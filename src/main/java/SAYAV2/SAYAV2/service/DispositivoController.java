package SAYAV2.SAYAV2.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.DispositivoDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;


public class DispositivoController{
	
	
private static DispositivoDao dispositivoDao;
private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
//private static File file = new File(FileUtils.getUsuarioFile());


	public DispositivoController() {
		super();
		dispositivoDao = new DispositivoDao();
	   
	}
		
	public static Route dispositivoVelocityEngine = (Request request, Response response) ->{
		
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();		
		
		DispositivoM dispositivo= new DispositivoM();
	    
		Usuario usuario = usuarioDao.cargar();

		model.put("user", usuario);
		model.put("dispositivo", dispositivo);
		model.put("listaDispositivos", usuario.getDispositivosMoviles());
		
        return ViewUtil.render(request, model, PathUtil.Template.DISPOSITIVO);
	};
	
	public static Route nuevoDispositivo = (Request request, Response response) ->{
		
		
		Map<String, Object> model = new HashMap<>();

		Usuario usuario;
		
		DispositivoM disp = new DispositivoM();
		
		disp = initDispositivo(request);
		//registrarDispositivo(disp);	
		
		//Se agrega el dispositivo a la lista de dispositivos	
		
		
		usuario = usuarioDao.cargar();
		if(disp.getNumero().toCharArray()[0] == '0'){
			
		}
		usuario.getDispositivosMoviles().add(disp);

		//Actualizo el Usuario
		UsuarioController.setCurrentUser(usuario);			
		UsuarioDao.getInstance().guardar(usuario);
		model.put("user", usuario);
		model.put("dispositivo", disp);		
		model.put("listaDispositivos", usuario.getDispositivosMoviles());
		return ViewUtil.render(request, model, PathUtil.Template.DISPOSITIVO);
		
	};
	
	
	public static Route eliminarDispositivo = (Request request, Response response) ->{
		
		Map<String, Object> model = new HashMap<>();
		System.out.println("Eliminar Dispositivo");
		//Se agrega el dispositivo a la lista de dispositivos		
		Usuario usuario = usuarioDao.cargar();
		String token = RequestUtil.getQueryDispositivoABorrar(request);
		
		if(usuarioDao.eliminarDispositivo(token)){
			String title = "Desvinculacion";
			String message = "Fue desvinculado de la central " + usuario.getSubdominio();
			usuario = usuarioDao.cargar();
			model.put("eliminarSuccess", true);
			model.put("user", usuario);
			model.put("listaDispositivos", usuario.getDispositivosMoviles());
			if(!FirebaseCloudMessageController.post(title, message, token)){
						
				//TODO reenvio
			}
			return ViewUtil.render(request, model, PathUtil.Template.DISPOSITIVO);
		}
		//Actualizo el Usuario
		model.put("user", usuario);
		model.put("eliminarFailed", true);
		model.put("listaDispositivos", usuario.getDispositivosMoviles()); 		
		return ViewUtil.render(request, model, PathUtil.Template.DISPOSITIVO);
		
	};

	private static DispositivoM initDispositivo(Request request){
		
		DispositivoM dispositivo = new DispositivoM();
		dispositivo.setNumero(RequestUtil.getQueryNumero(request));		
		return dispositivo;
	}
	
	
	public static String registrarDispositivo(DispositivoM dispositivo) {
		
		if(dispositivoDao == null){
			dispositivoDao = new DispositivoDao();
		}

		File file;
		
		
		file = new File("DM "+dispositivo.getNumero());
		file.setWritable(true);
		file.setReadable(true);
	    
	    dispositivoDao.persist(dispositivo, file);
		
	    
	    return "Dispositivo guardado";
	}
	
	public String renderContent(String htmlFile) {
		   
		try {
	        // If you are using maven then your files
	        // will be in a folder called resources.
	        // getResource() gets that folder
	        // and any files you specify.
	       // URL url = getClass().getResource(htmlFile);
	        // Return a String which has all
	        // the contents of the file.
	        Path path = Paths.get(htmlFile);
	        byte[] array = Files.readAllBytes(path);
	        String result = new String(array, Charset.defaultCharset());
	        return result;
	    } catch (IllegalArgumentException| OutOfMemoryError | SecurityException | IOException e) {
	    // Add your own exception handlers here.
	    }
	    return null;
	}		
	
	
}
