package SAYAV2.SAYAV2.service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.TipoMensaje;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.MensajePendienteDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.GrupoPeer;
import SAYAV2.SAYAV2.model.Mensaje;
import SAYAV2.SAYAV2.model.MensajePendienteData;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class GrupoController {

	private static JsonTransformer jsonTransformer = new JsonTransformer();
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static MensajePendienteDao mDao = MensajePendienteDao.getInstance();
	private static File file = new File("SAYAV");
	private static File mensajesRecibidos = new File("MensajesRecibidos");
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
		model.put("user", usuario);
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
		model.put("user", usuario);
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
			// que trae el mensaje, lo que indica a que grupo pertenece ese
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
			
			if (usuario.getAlarmaHabilitada()) {
				// Recorre la lista de peers de los grupos a fin de notificar a
				// todos
				String message = "El boton de panico ha sido activado en el domicilio " + usuario.getDireccion()
						+ "\nEl dueño del domicilio es " + usuario.getNombre() + " " + usuario.getApellido();
				FirebaseCloudMessageController.post("Peligro", message);
			}else{
				Date date = new Date();
				DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"); 

				MensajePendienteData data = new MensajePendienteData();
				data.setPeer(mensaje.getOrigen());
				data.setFecha(hourdateFormat.format(date));
				
				MensajesPendientes mens;
				
				if(( mens = mDao.cargar(mensajesRecibidos)) == null){
					mens = new MensajesPendientes();
				}
				
				Mensaje nuevo = mensaje.clone();
				nuevo.setDatos(jsonTransformer.render(data));

				mens.addMensaje(nuevo);
				mDao.guardar(mens, mensajesRecibidos);
			}
		}

		if (mensaje.getTipo().equals(TipoMensaje.NUEVO_GRUPO)) {
			GrupoPeer data = jsonTransformer.getGson().fromJson(mensaje.getDatos(), GrupoPeer.class);

			
			if (usuario.addGrupo(new Grupo(data.getGrupo()))) {
				Grupo g = usuario.getSingleGrupo(data.getGrupo());
				g.addAll(data.getListaPeers());
				usuarioDao.guardar(usuario, file);
			}
			else{
				usuario.addGrupo(data.getGrupo()+"_Rename");
				Grupo g = usuario.getSingleGrupo(data.getGrupo());
				g.addAll(data.getListaPeers());
				usuarioDao.guardar(usuario, file);
				model.put("user", usuario);
				model.put("existingGroupRename", true);
				
			}

			model.put("user", usuario);
			return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
		}
		
		if(mensaje.getTipo().equals(TipoMensaje.CHECK_CONNECTIVITY)){
//			Mensaje respuesta = new Mensaje();
//			respuesta.setOrigen(usuario.getSubdominio());
//			if(usuario.getAlarmaHabilitada()){
//				respuesta.setTipo(TipoMensaje.CONECTADO);
//			}else{
//				respuesta.setTipo(TipoMensaje.DESCONECTADO);
//			}
//			PostGrupo.post(mensaje.getOrigen(), respuesta);
			response.redirect(request.url());
		}

		return ViewUtil.render(request, model, PathUtil.Template.PRUEBA);
	};

}
