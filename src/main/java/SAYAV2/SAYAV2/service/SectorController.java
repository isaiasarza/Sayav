package SAYAV2.SAYAV2.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.FileUtils;
import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.ConfiguratorDao;
import SAYAV2.SAYAV2.dao.TipoMensajeDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.Configurator;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Sector;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.notificacion.GruposImpl;
import spark.Request;
import spark.Response;
import spark.Route;

public class SectorController {
	
	//private static NotificationEvent event;
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static GruposImpl grupos = GruposImpl.getInstance();
	private static TipoMensajeDao tiposDao = TipoMensajeDao.getInstance();
	private static Configurator configurator ; 
	public SectorController() {
		super();
		try {
			configurator = ConfiguratorDao.getInstance().cargar(FileUtils.CONFIGURATOR_FILE);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Route sectorVelocityEngine = (Request request, Response response) -> {

		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Sector sector = new Sector();

		Usuario usuario = usuarioDao.cargar();

		int numeroSectores = 0;
		model.put("user", usuario);
		model.put("numeroSectores", numeroSectores);
		model.put("sector", sector);
		model.put("listaSectores", usuario.getSectores());

		return ViewUtil.render(request, model, PathUtil.Template.SECTOR);
	};

	public static Route numeroSectores = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);

		Map<String, Object> model = new HashMap<>();

		Usuario usuario;
		int numeroSectores = RequestUtil.getQueryNumeroSectores(request);

		usuario = usuarioDao.cargar();
		// Cada vez que renuevo el numero de sectores, limpio la lista existente
		usuario.getSectores().clear();

		model.put("numeroSectores", numeroSectores);
		// Voy creando el numero de sectores definidos por el usuario
		Sector sector;
		for (int i = 1; i < numeroSectores; i++) {
			sector = new Sector();
			sector.setNombre("n" + i);
			sector.setActivado(false);
			usuario.getSectores().add(sector);
		}
		sector = new Sector();
		sector.setNombre("Boton de Panico");
		usuario.getSectores().add(sector);
		UsuarioController.setCurrentUser(usuario);
		usuarioDao.guardar(usuario);
		model.put("user", usuario);
		model.put("listaSectores", usuario.getSectores());

		return ViewUtil.render(request, model, PathUtil.Template.SECTOR);

	};

	@SuppressWarnings("unused")
	private static Sector initSector(Request request) {

		Sector sector = new Sector();
		sector.setNombre(RequestUtil.getQueryNombre(request));

		return sector;
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

	public static Route renombrarSector = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);

		Map<String, Object> model = new HashMap<>();
		System.out.println("Renombrar Sector");
		Usuario usuario;
		// Se agrega el dispositivo a la lista de dispositivos
		usuario = usuarioDao.cargar();
		String nombreNuevo = RequestUtil.getQueryNuevoNombre(request);
		String nombreActual = RequestUtil.getQuerySectorRenombrar(request);
		System.out.println(nombreActual);
		System.out.println(nombreNuevo);

		if (usuario.existSector(nombreNuevo)) {

			System.out.println("Nombre existente");
			model.put("existingSector", true);
			RequestUtil.removeSessionSectores(request);
			model.put("user", usuario);
			model.put("listaSectores", usuario.getSectores());
			return ViewUtil.render(request, model, PathUtil.Template.SECTOR);

		}
		buscarSector(nombreActual, nombreNuevo, usuario.getSectores());

		System.out.println(UsuarioController.getCurrentUser());
		usuarioDao.guardar(usuario);
		RequestUtil.removeSessionSectores(request);
		model.put("user", usuario);
		model.put("listaSectores", usuario.getSectores());

		return ViewUtil.render(request, model, PathUtil.Template.SECTOR);

	};
	
	@SuppressWarnings("unused")
	public static Route cambiarEstado = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Thread thread;
		configurator = ConfiguratorDao.getInstance().cargar(FileUtils.CONFIGURATOR_FILE);

		Map<String, Object> model = new HashMap<>();
		String nombreSector = request.params("nombre");

		System.out.println("Cambiar Estado Sector " + nombreSector);

		Usuario usuario = usuarioDao.cargar();
		Sector sector = usuario.getSector(nombreSector);

		if (usuario.isAlarmaHabilitada()) {
			sector.cambiarEstado();
			usuarioDao.guardar(usuario);
			RequestUtil.removeSessionSectores(request);
			//tiposDao.setFile(tiposFile);

			String titulo, message;
			// Notifica Dispositivos Moviles
			if (sector.isActivado()) {
				titulo = "Peligro!";
				message = "Se disparo alarma en el domicilio " + usuario.getDireccion() + ":El dueño del domicilio es "
						+ usuario.getNombre() + " " + usuario.getApellido() + " " + ":Sector " + sector.getNombre();
			} else {
				titulo = "Calmaos!";
				message = "El domicio del camarada " + usuario.getNombre() + usuario.getApellido() + " fue intervenido:";
			}

			//FirebaseCloudMessageController.post(titulo, message);
			Peer origen = new Peer(usuario.getSubdominio(),configurator.getPort());
			Mensaje mensaje = new Mensaje();
			mensaje.setOrigen(origen);
			mensaje.setDescripcion(message);
			mensaje.setEstado(EstadoUtils.Estado.PENDIENTE);
			mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
			mensaje.setTipoMensaje(tiposDao.getTipo(TipoMensajeUtils.ALERTA,FileUtils.TIPOS_MENSAJES_FILE));
	
			if(!grupos.isInit()){
				grupos.init();
			}
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
					    grupos.notificarMoviles(usuario.getDispositivosMoviles(), mensaje);
						grupos.notificarGrupos(usuario.getGrupos(), mensaje);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, "Notificando sobre alarma");
			thread.start();
		}

		model.put("sector", sector);
		model.put("user", usuario);
		model.put("listaSectores", usuario.getSectores());

		return ViewUtil.render(request, model, PathUtil.Template.SECTOR);

	};

	private static void buscarSector(String nombreActual, String nombreNuevo, List<Sector> sectores) {

		for (Sector s : sectores) {
			if (s.getNombre().equals(nombreActual)) {
				s.setNombre(nombreNuevo);
				return;
			}
		}
	}

	public void estadoOk(List<Sector> sectores) {

		for (Sector sector : sectores)
			sector.setActivado(false);
	}

}
