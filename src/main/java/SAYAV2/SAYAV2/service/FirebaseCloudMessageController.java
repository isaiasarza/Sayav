package SAYAV2.SAYAV2.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.dao.NotificacionesDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Notificacion;
import SAYAV2.SAYAV2.model.NotificationMovil;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class FirebaseCloudMessageController {


	private static String apiKey = "AAAAi0VYU24:APA91bF8chF5cS1N0ialjG1hqD_yB8EU6hMAmtbabowP5Izzrm5VK6wYlMr1z1YgVndHiwBEsaVT-jotwyjU9JQxG1z0sXlyWHDpz-HU5aehgjhdxmwZ_a-_KtDtXPgp54MN6TN5IG0o";
	private static JsonTransformer json = new JsonTransformer();
	private static NotificacionesDao notisDao = NotificacionesDao.getInstance();
	// private static File notis = new File(FileUtils.getNotificacionesFile());
	static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	// private static File file = new File(FileUtils.getUsuarioFile());

	public static Route getNewToken = (Request request, Response response) -> {
		System.out.println("Llego Al Get");
		return "Get Token";
	};

	public static Route postNewToken = (Request request, Response response) -> {
		System.out.println("Llego Al Post");

		String token = request.params("token");

		if (token != null) {
			añadirDispositivo(token);
		}

		Notificacion n = new Notificacion();
		n.setTipo(TipoMensajeUtils.NUEVO_DISPOSITIVO);
		n.setDescripcion("Se vinculo un nuevo dispositivo:El token del mismo es:" + token);
		// notisDao.setFile(notis);
		notisDao.agregarNotificacion(n);
		return null;
	};

	public static Route pushNotification = (Request request, Response response) -> {
		System.out.println("Llego Al Get");
		//post(TITLE, MESSAGE);
		return null;
	};

	public static boolean post(String title, Mensaje msg) {
		List<String> registration_ids = getTokens();
		NotificationMovil notification = new NotificationMovil(registration_ids, title, json.render(msg));
		if (!notification.getRegistration_ids().isEmpty()) {
			return PostGCM.post(apiKey, notification);
		}
		return false;
	}

	private static void añadirDispositivo(String token) throws JAXBException, IOException {
		DispositivoM d = new DispositivoM(token);
		Usuario usuario = usuarioDao.cargar();
		if (!usuario.getDispositivosMoviles().contains(d)) {
			usuario.getDispositivosMoviles().add(d);
			usuarioDao.guardar(usuario);
		}
	}

	private static List<String> getTokens() {
		List<DispositivoM> dispositivos = getDispositivos();
		List<String> tokens = new ArrayList<String>();
		if (dispositivos != null) {
			for (DispositivoM d : dispositivos) {
				if (d.getToken() != null || d.getToken() != "") {
					tokens.add(d.getToken());
				}
			}
		}
		return tokens;
	}

	private static List<DispositivoM> getDispositivos() {
		Usuario u = null;
		try {
			u = (Usuario) usuarioDao.cargar();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return u.getDispositivosMoviles();
	}

	public static boolean post(String title, String message, String token) {
		List<String> registration_ids = new LinkedList<String>();
		registration_ids.add(token);
		NotificationMovil notification = new NotificationMovil(registration_ids, title, message);
		if (!notification.getRegistration_ids().isEmpty()) {
			return PostGCM.post(apiKey, notification);
		}
		return false;
	}
}
