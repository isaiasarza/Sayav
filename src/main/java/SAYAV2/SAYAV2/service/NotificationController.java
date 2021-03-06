package SAYAV2.SAYAV2.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.Utils.FileUtils;
import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.NotificacionesDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Notificaciones;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class NotificationController {
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	//private static File usuarioFile = new File(FileUtils.getUsuarioFile());
	private static NotificacionesDao notificacionDao = NotificacionesDao.getInstance();
	//private static File notificacionesFile = new File(FileUtils.getNotificacionesFile());
	
	public static Route mostrarNotificacion = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		
		Usuario usuario = usuarioDao.cargar();
		Notificaciones notificaciones = null;
		try{
			notificaciones = notificacionDao.cargar(FileUtils.NOTIFICACIONES_FILE);
			if(notificaciones == null){
				notificaciones = new Notificaciones();	
			}else{
				Collections.sort(notificaciones.getNotificacion());
				Collections.reverse(notificaciones.getNotificacion());
			}

		} catch (JAXBException e) {
			e.printStackTrace();
			notificaciones = new Notificaciones();
		}
		model.put("detalle", "holi");
		model.put("notificaciones",notificaciones);
		model.put("user", usuario);
		model.put("ver", false);
		return ViewUtil.render(request, model, PathUtil.Template.SHOW_NOTIFICATION);

	};
	

	
	

}
