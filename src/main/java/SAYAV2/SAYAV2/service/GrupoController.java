package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.TipoMensaje;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.GrupoPeer;
import SAYAV2.SAYAV2.model.Mensaje;
import SAYAV2.SAYAV2.model.Notification;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class GrupoController {

	private static JsonTransformer jsonTransformer = new JsonTransformer();
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static File file = new File("SAYAV");

	public static Route grupoVelocityEngine = (Request request, Response response) -> {

		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		Grupo grupo = new Grupo();

		Usuario usuario;

		usuario = UsuarioController.getCurrentUser();

		model.put("grupo", grupo);

		model.put("listaGrupos", usuario.getGrupos());

		return ViewUtil.render(request, model, PathUtil.Template.GRUPO);
	};

	public static Route nuevoGrupo = (Request request, Response response) -> {

		Map<String, Object> model = new HashMap<>();

		Usuario usuario;

		Grupo grupo = new Grupo();

		grupo = initGrupo(request);

		usuario = UsuarioController.getCurrentUser();

		usuario.getGrupos().add(grupo);

		// Actualizo el Usuario
		UsuarioController.setCurrentUser(usuario);

		UsuarioDao.getInstance().guardar(usuario, UsuarioController.getFile());

		model.put("grupo", grupo);
		model.put("listaGrupos", usuario.getGrupos());

		return ViewUtil.render(request, model, PathUtil.Template.GRUPO);

	};

	private static Grupo initGrupo(Request request) {

		Usuario usuario = new Usuario();

		usuario = UsuarioController.getCurrentUser();

		Grupo grupo = new Grupo();
		grupo.setId(usuario.getGrupos().size() + 1);
		grupo.setNombre(SAYAV2.SAYAV2.Utils.RequestUtil.getQueryNombreGrupo(request));
		return grupo;
	}

	public static Grupo buscarGrupo(int id) {

		Usuario usuario;

		usuario = UsuarioController.getCurrentUser();
		for (Grupo g : usuario.getGrupos()) {
			if (g.getId() == id)
				return g;
		}

		return null;
	}

	public static Route getNotificar = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar(file);
		String url = "http://127.0.0.1:29080" + PathUtil.Web.GRUOP_NOTIFICATION;
		System.out.println("Notificando");
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(url);
		mensaje.setTipo(TipoMensaje.ALERTA);
		mensaje.setDescripcion("Nuevo Peer");
		mensaje.setDatos("http://127.0.0.2:29080");

		PostGrupo.post("http://isaiasarzauni.ddns.net:29100" + PathUtil.Web.GRUOP_NOTIFICATION, mensaje);
		model.put("message", mensaje);
		return ViewUtil.render(request, model, PathUtil.Template.PRUEBA);
	};
	public static Route notificar = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar(file);
		Mensaje mensaje = jsonTransformer.getGson().fromJson(request.body(), Mensaje.class);
		System.out.println(mensaje);
		if (mensaje.getTipo().equals(TipoMensaje.BAJA_MIEMBRO)) {
			// Recorre la lista de peers de los grupos en busca de un peer que
			// contenga la direccion de origen
			// que trae le mensaje, lo que indica a que grupo pertenece ese
			// nuevo peer a cargar (la direccion viene en Datos)
			GrupoPeer data = jsonTransformer.getGson().fromJson(mensaje.getDatos(), GrupoPeer.class);
			Grupo g = usuario.getSingleGrupo(data.getGrupo());
			g.removePeer(data.getPeer());
			usuarioDao.guardar(usuario, file);
		}
		if (mensaje.getTipo().equals(TipoMensaje.NUEVO_MIEMBRO)) {
			// Recorre la lista de peers de los grupos en busca de un peer que
			// contenga la direccion de origen
			// que trae le mensaje, lo que indica a que grupo pertenece ese
			// nuevo peer a cargar (la direccion viene en Datos)
			GrupoPeer data = jsonTransformer.getGson().fromJson(mensaje.getDatos(), GrupoPeer.class);
			Grupo g = usuario.getSingleGrupo(data.getGrupo());
			g.addPeer(data.getPeer());
			usuarioDao.guardar(usuario, file);
		}
		if (mensaje.getTipo().equals(TipoMensaje.ALERTA)) {
			// Recorre la lista de peers de los grupos a fin de notificar a
			// todos
			Notification notificacion = new Notification();
			String message = "El boton de panico ha sido activado en el domicilio " + usuario.getDireccion()
			+ "\nEl dueño del domicilio es " + usuario.getNombre() + " " + usuario.getApellido();
			FirebaseCloudMessageController.post("Peligro", message);
//			List<String> registration_ids = usuario.getTokens();
//			notificacion.setRegistrationIds(registration_ids);
//			notificacion.createData(, "Alerta");
//			for (DispositivoM d : usuario.getDispositivosMoviles())
//				PostGCM.post("", notificacion);
			return null;
		}

		if(mensaje.getTipo().equals(TipoMensaje.NUEVO_GRUPO)){
			GrupoPeer data = jsonTransformer.getGson().fromJson(mensaje.getDatos(), GrupoPeer.class);
			usuario.addGrupo(data.getGrupo());
			Grupo g = usuario.getSingleGrupo(data.getGrupo());
			g.addAll(data.getListaPeers());
			usuarioDao.guardar(usuario, file);
		}
		
		return ViewUtil.render(request, model, PathUtil.Template.PRUEBA);
	};

}
