package SAYAV2.SAYAV2.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Sector;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class SectorController {

	private static Sector sec;
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static File file = new File("SAYAV");
	
	public SectorController() {
		// TODO Auto-generated constructor stub
		super();
	}

	public static Route sectorVelocityEngine = (Request request, Response response) -> {
		
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Sector sector = new Sector();

		Usuario usuario = usuarioDao.cargar(file);
		
		int numeroSectores=0;
		model.put("user",usuario);
        model.put("numeroSectores", numeroSectores);
		model.put("sector", sector);
		model.put("listaSectores", usuario.getSectores());

		return ViewUtil.render(request, model, PathUtil.Template.SECTOR);
	};

	public static Route numeroSectores = (Request request, Response response) -> {

		Map<String, Object> model = new HashMap<>();

		Usuario usuario;
		int numeroSectores= RequestUtil.getQueryNumeroSectores(request);
		
		
		usuario = usuarioDao.cargar(file);
		//Cada vez que renuevo el numero de sectores, limpio la lista existente
		usuario.getSectores().clear();
		
		model.put("numeroSectores", numeroSectores);
		//Voy creando el numero de sectores definidos por el usuario
		for(int i=1; i<=numeroSectores; i++){	
			Sector sector = new Sector();
			sector.setId(i);
			sector.setNombre("n"+i);
			sector.setActivado(false);
			usuario.getSectores().add(sector);
		}
		UsuarioController.setCurrentUser(usuario);
		usuarioDao.guardar(usuario, file);
		//UsuarioDao.getInstance().guardar(usuario, UsuarioController.getFile());
		model.put("user", usuario);
		model.put("listaSectores", usuario.getSectores());

		return ViewUtil.render(request, model, PathUtil.Template.SECTOR);

	};

	private static Sector initSector(Request request) {

		Sector Sector = new Sector();
		Sector.setNombre(SAYAV2.SAYAV2.Utils.RequestUtil.getQueryNombre(request));

		return Sector;
	}

	public String renderContent(String htmlFile) {

		try {
		
			Path path = Paths.get(htmlFile);
			byte[] array = Files.readAllBytes(path);
			String result = new String(array, Charset.defaultCharset());
			return result;
		} catch (IllegalArgumentException | OutOfMemoryError | SecurityException | IOException e) {
			
		}
		return null;
	}
	
	public static Route renombrarSector = (Request request, Response response) ->{
		
		Map<String, Object> model = new HashMap<>();
		System.out.println("Renombrar Sector");
		Usuario usuario;
		//Se agrega el dispositivo a la lista de dispositivos		
		usuario = usuarioDao.cargar(file);
		String nombreNuevo = RequestUtil.getQueryNuevoNombre(request);
		String nombreActual = RequestUtil.getQuerySectorRenombrar(request);
		System.out.println(nombreActual);
		System.out.println(nombreNuevo);
		
		if(usuario.existSector(nombreNuevo)){

			System.out.println("Nombre existente");
			model.put("existingSector", true);
			RequestUtil.removeSessionSectores(request);
			model.put("user", usuario);
			model.put("listaSectores", usuario.getSectores()); 
			return ViewUtil.render(request, model, PathUtil.Template.SECTOR);

		}
		buscarSector(nombreActual,nombreNuevo,usuario.getSectores());
		
	
		System.out.println(UsuarioController.getCurrentUser());
		usuarioDao.guardar(usuario, file);
		RequestUtil.removeSessionSectores(request);
		model.put("user", usuario);
		model.put("listaSectores", usuario.getSectores()); 
		
		return ViewUtil.render(request, model, PathUtil.Template.SECTOR);
		
	};
	public static Route cambiarEstado = (Request request, Response response) ->{
		Map<String, Object> model = new HashMap<>();
		String nombreSector = request.params("nombre");
		
		System.out.println("Cambiar Estado Sector " + nombreSector);
		
		Usuario usuario = usuarioDao.cargar(file);
		usuario.getSector(nombreSector).cambiarEstado();
		usuarioDao.guardar(usuario, file);
		RequestUtil.removeSessionSectores(request);
		model.put("user", usuario);
		model.put("listaSectores", usuario.getSectores()); 
		return ViewUtil.render(request, model, PathUtil.Template.SECTOR);
	};

	private static void buscarSector(String nombreActual, String nombreNuevo,List<Sector> sectores){
	
		for(Sector s: sectores){
			if(s.getNombre().equals(nombreActual)){
				s.setNombre(nombreNuevo);
				return;
			}
		}
	}
	
}