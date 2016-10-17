package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class AlarmController {
	private static UsuarioDao usuarioDao;
	private static File file = new File("SAYAV");
	
	public static Route enableAlarmPost = (Request request, Response response) -> {
		usuarioDao = UsuarioDao.getInstance();
//        request.session().removeAttribute("currentUser");
//        request.session().attribute("loggedOut", true);
		Map<String, Object> model = new HashMap<>();
		model.put("enableAlarm", RequestUtil.getQueryAlarmStatus(request));
//        String name = RequestUtil.getQueryName(request);
//        String lastname = RequestUtil.getQueryLastName(request);
//       File f = new File(name + " " + lastname);
        Usuario usuario;
        usuario = (Usuario) usuarioDao.cargar(file);
        usuario.setAlarmaHabilitada(RequestUtil.getQueryAlarmStatus(request));
        usuarioDao.guardar(usuario,file);
        response.redirect(PathUtil.Web.ALARM);
        return null;
    };
}
