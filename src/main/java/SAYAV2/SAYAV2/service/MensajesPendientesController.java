package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.MensajePendienteDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Mensaje;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class MensajesPendientesController {
	
	private static MensajePendienteDao mensajeDao = MensajePendienteDao.getInstance();
	private static File fileMensajes = new File("Mensajes");
	private static File file = new File("SAYAV");
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	
	
	
	public static Route getAllMenssages = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Mensaje mensaje = new Mensaje();
	    Usuario usuario = usuarioDao.cargar(file);
		MensajesPendientes mp = mensajeDao.cargar(fileMensajes);
		
		
		model.put("user", usuario);		
		model.put("mensaje", mensaje);	
		model.put("mensajesPendientes", mp);
		model.put("listaMensajes", mp.getListaMensajes());

		System.out.println(response.raw().getStatus());

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_MESSAGES);

	};
	
	

}
