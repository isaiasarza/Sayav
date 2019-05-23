package SAYAV2.SAYAV2.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.Utils.FileUtils;
import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.bussines.Alarma;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class AlarmController {
	private static UsuarioDao usuarioDao;
	//private static File file = new File(FileUtils.getUsuarioFile());

	/**
	 * Habilita o deshabilita el alarma
	 */
	public static Route enableAlarmPost = (Request request, Response response) -> {
		usuarioDao = UsuarioDao.getInstance();
		System.out.println("Alarm change status");

		Map<String, Object> model = new HashMap<>();

		Usuario usuario;
		usuario = usuarioDao.cargar(FileUtils.USUARIO_FILE);
		boolean status = usuario.isAlarmaHabilitada();
		usuario.setAlarmaHabilitada(!status);

		// Cambio estado de sectores de acuerdo a la alarma (A/D)
		SectorController sec = new SectorController();
		sec.estadoOk(usuario.getSectores());
		usuarioDao.guardar(usuario, FileUtils.USUARIO_FILE);
		UsuarioController.setCurrentUser(usuario);

		model.put("user", usuario);
		Usuario u = request.session().attribute("user");
		System.out.println("Usuario " + u);
		return ViewUtil.render(request, model, PathUtil.Template.MENU);
	};

	public static Route panicButton = (Request request, Response response) -> {
		usuarioDao = UsuarioDao.getInstance();
		System.out.println("Panic Button");

		Map<String, Object> model = new HashMap<>();

		Usuario usuario;
		usuario = usuarioDao.cargar(FileUtils.USUARIO_FILE);
		Mensaje msg = new Mensaje() ;
		//msg.setOrigen(new Peer(usuario.getSubdominio(), ));
		
		msg.setDescripcion("El boton de panico ha sido activado en el domicilio "+ usuario.getDireccion()
				+ "\nEl dueño del domicilio es " + usuario.getNombre() + " " + usuario.getApellido() );
		
		FirebaseCloudMessageController.post("Peligro!", msg);
		Alarma.notificar(usuario);

		//notificarCentrales("Peligro!", message);
		model.put("panicButton", true);
		model.put("user", usuario);
		return ViewUtil.render(request, model, PathUtil.Template.MENU);
	};

	private static void notificarCentrales(String titulo, String message)
			throws JAXBException, ProtocolException, MalformedURLException, IOException {
		Mensaje mensaje = new Mensaje();
		notificarCentrales(mensaje);
	}

	public static void notificarCentrales(Mensaje mensaje)
			throws JAXBException, ProtocolException, MalformedURLException, IOException {

		Usuario usuario = usuarioDao.cargar(FileUtils.USUARIO_FILE);
		

//		if (!usuario.getGrupos().isEmpty()) {
//			for (Grupo grupo : usuario.getGrupos()) {
//				if (!grupo.getPeers().isEmpty()) {
//					for (Peer peer : grupo.getPeers()) {
//						System.out.println("Notificando Peer: " + peer.getDireccion());
//						PostGrupo.post("http://" + peer.getDireccion() + PathUtil.Web.GRUOP_NOTIFICATION, mensaje);
//					}
//				}
//			}
//		}
	}
}
