package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Notification;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class FirebaseCloudMessageController {

	private static String TITLE = "Test Title";
	private static String MESSAGE = "Test Message";
	private static String apiKey = "AIzaSyCNKGXQDvhI24i2SwDq4QqsgOS1Fx1TBFI";

	

	static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static File file = new File("SAYAV");

	public static Route getNewToken = (Request request, Response response) -> {
		System.out.println("Llego Al Get");
		return "Get Token";
	};

	public static Route postNewToken = (Request request, Response response) -> {
		System.out.println("Llego Al Post");

		String token = request.params("token");
		String body = request.queryParams("Token");


		if (body != null) {
			createToken(body);
		} else {
			if (token != null)
				createToken(token);
		}

		return null;
	};

	public static Route pushNotification = (Request request, Response response) -> {
		System.out.println("Llego Al Get");
		post(TITLE,MESSAGE);
		return null;
	};
	
	public static void post(String title, String message){
		List<String> registration_ids = getTokens();
		Notification notification = new Notification(registration_ids, TITLE, MESSAGE);
		if (!notification.getRegistration_ids().isEmpty()) {
			PostGCM.post(apiKey, notification);
		}
	}

	private static void createToken(String token) throws JAXBException {
		DispositivoM d = new DispositivoM(token);
		Usuario usuario = usuarioDao.cargar(file);
		usuario.getDispositivosMoviles().add(d);
		usuarioDao.guardar(usuario, file);
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
			u = (Usuario) usuarioDao.cargar(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return u.getDispositivosMoviles();
	}
}
