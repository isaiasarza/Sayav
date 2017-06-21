package SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import SAYAV2.Utils.EstadoUtils;
import SAYAV2.Utils.FileUtils;
import SAYAV2.Utils.PathUtil;
import SAYAV2.Utils.ViewUtil;
import SAYAV2.dao.MensajePendienteDao;
import SAYAV2.dao.UsuarioDao;
import SAYAV2.mensajeria.Mensaje;
import SAYAV2.model.MensajesPendientes;
import SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class MensajesPendientesController {

	private static MensajePendienteDao mensajeDao = MensajePendienteDao.getInstance();
	private static File fileMensajes = new File("Mensajes_test");
	private static File file = new File(FileUtils.getUsuarioFile());
	//private static JsonTransformer jsonTransformer = new JsonTransformer();
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();

	public static Route getAllMenssages = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Mensaje mensaje = new Mensaje();
		Usuario usuario = usuarioDao.cargar(file);
		MensajesPendientes mp = mensajeDao.cargar(fileMensajes);
        //List<MensajeDato> listaMensajes = new ArrayList<MensajeDato>();
		
	

		model.put("user", usuario);
		model.put("mensaje", mensaje);
		model.put("mensajesPendientes", mp);
		//model.put("estado", EstadoUtils.PENDIENTE);
		model.put("estados",EstadoUtils.class);

		//model.put("listaMensajes", listaMensajes);

		//System.out.println(response.raw().getStatus());

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_MESSAGES);

	};

}
