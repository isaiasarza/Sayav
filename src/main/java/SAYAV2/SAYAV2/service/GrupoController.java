package SAYAV2.SAYAV2.service;

import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.TipoMensaje;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Mensaje;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class GrupoController {
	
	private static JsonTransformer jsonTransformer = new JsonTransformer();
	
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
		grupo.setId(usuario.getGrupos().size()+1);
		grupo.setNombre(SAYAV2.SAYAV2.Utils.RequestUtil.getQueryNombreGrupo(request));
		return grupo;
	}	
	
	
	
	public static Grupo buscarGrupo(int id){

		Usuario usuario;
		
		usuario = UsuarioController.getCurrentUser();
		for(Grupo g: usuario.getGrupos()){
			if(g.getId()==id)
				return g;
		}
		
		return null;
	}	
	public static Route getNotificar = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();

		String url = "http://192.168.1.38:29100" + PathUtil.Web.GRUOP_NOTIFICATION;
		System.out.println("Notificando");
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen("192.168.1.37");
		mensaje.setTipo("Prueba");
		mensaje.setDescripcion("Esto es una prueba");
		PostGrupo.post(url,mensaje);
		return ViewUtil.render(request, model, PathUtil.Template.PRUEBA);
	};
	public static Route notificar = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();
		Mensaje mensaje = jsonTransformer.getGson().fromJson(request.body(), Mensaje.class);
		if(mensaje.getTipo().equals(TipoMensaje.BAJA_MIEMBRO)){
			//TODO
			return null;
		}
		if(mensaje.getTipo().equals(TipoMensaje.NUEVO_MIEMBRO)){
			//TODO
			return null;
		}
		if(mensaje.getTipo().equals(TipoMensaje.ALERTA)){
			//TODO
			return null;
		}
		
		return ViewUtil.render(request, model, PathUtil.Template.PRUEBA);
	};

}
