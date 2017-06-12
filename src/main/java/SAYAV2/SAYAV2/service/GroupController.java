package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.validator.routines.DomainValidator;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.MensajePendienteDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.dao.VotacionesDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.notificacion.GruposImpl;
import SAYAV2.SAYAV2.notificacion.Votacion;
import SAYAV2.SAYAV2.notificacion.Votaciones;
import spark.Request;
import spark.Response;
import spark.Route;

public class GroupController {

	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static File file = new File("SAYAV");
	private static GruposImpl grupos = new GruposImpl();
	private static File votacionesPendientesFile = new File("votaciones_pendientes");
	private static File votacionesFile = new File("votaciones"); 
	private static File mensajesFile = new File("Mensajes"); 
	private static VotacionesDao votacionesDao = VotacionesDao.getInstance();
	private static MensajePendienteDao mensajesDao = MensajePendienteDao.getInstance(); 

	public static Route getNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar(file);
		System.out.println(usuario);

		Mensaje mensaje = new Mensaje();
		model.put("mensaje", mensaje);

		model.put("user", usuario);
		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);

	};

	public static Route postNewGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		Usuario usuario = usuarioDao.cargar(file);
		String nombre = RequestUtil.getNewGroupName(request);
        
		Grupo nuevoGrupo = new Grupo(nombre);
		
		if(nombre.contains("/")){
			model.put("invalidUsername", true);
			model.put("user", usuario);
			return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
		}		 
		if (usuario.addGrupo(nuevoGrupo)) {
			model.put("addGroupSucceeded", true);
			model.put("user", usuario);
			usuarioDao.guardar(usuario, file);
		} else {
			model.put("user", usuario);
			model.put("existingGroup", true);
		}

		return ViewUtil.render(request, model, PathUtil.Template.NEW_GROUP);
	};

	public static Route getAllGroups = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar(file);

		model.put("user", usuario);

		//System.out.println(response.raw().getStatus());

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_GROUPS);

	};

	public static Route leaveGroup = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Usuario usuario = usuarioDao.cargar(file);
		String groupName = request.params("groupName");
		Grupo grupo = usuario.getSingleGrupoByName(groupName);
		if(!grupos.isInit()){
			grupos.init();
		}

		if(!grupo.getPeers().isEmpty()){
			grupos.abandonarGrupo(grupo);
		}
		usuarioDao.eliminarGrupo(grupo);
		request.session().removeAttribute("user");
		request.session().attribute("user",usuario);
		request.session().attribute("leaveGroupSucceeded", true);

		response.redirect(PathUtil.Web.NEW_GROUP);
		return null;

	};
	public static Route getNewGroupMember = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		Usuario usuario = usuarioDao.cargar(file);
		String groupName = request.params("groupName");

		model.put("user", usuario);
		model.put("groupName", groupName);

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);

	};

	public static Route postNewGroupMember = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

		System.out.println("Agregando Miembro");
		String memberDomain, groupName;
		Usuario usuario = usuarioDao.cargar(file);
		groupName = request.params("groupName");

		Grupo grupo = usuario.getSingleGrupoByName(groupName);
		DomainValidator domainValidator = DomainValidator.getInstance(false);
		memberDomain = RequestUtil.getQueryMemberDomain(request);
		
		if(memberDomain.contains("/")){
			model.put("invalidDomain", true);
			model.put("user", usuario);
			model.put("group", grupo);
			model.put("groupName", groupName);
			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}
		
		if(!domainValidator.isValid(memberDomain)){
			model.put("invalidDomain", true);
			model.put("user", usuario);
			model.put("group", grupo);
			model.put("groupName", groupName);
			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}
		
		
		model.put("groupName", groupName);
		model.put("group", grupo);
		model.put("user", usuario);
		

		if(memberDomain.equals(usuario.getSubdominio()) || memberDomain.equals("localhost:")){
			System.out.println("El miembro es el mismo usuario");
			model.put("memberIsUser", true);
			model.put("user", usuario);

			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}
		
		Peer peer = new Peer();
		peer.setDireccion(memberDomain);
		
		if(grupo.getPeers().contains(peer)){
			model.put("existingMember", true);
			model.put("user", usuario);

			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
		}

		if(!grupos.isInit()){
			grupos.init();
		}
		if(!grupos.añadirMiembro(grupo, peer)){
			model.put("procesandoMiembro", true);
			model.put("user", usuario);
			model.put("group", grupo);
			return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);

		}
		
		RequestUtil.removeSessionAttrUser(request);
		model.put("addMemberSucceeded", true);
		model.put("user", usuario);
		model.put("group", grupo);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);

	};

	public static Route getViewMembers = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("View Members");
		Usuario usuario = usuarioDao.cargar(file);
		String groupName = request.params("groupName");
		Grupo group = usuario.getSingleGrupoByName(groupName);

		model.put("groupName", groupName);
		model.put("group", group);
		model.put("user", usuario);

		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
	};

	public static Route solicitarBaja = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		
		if(!grupos.isInit()){
			grupos.init();
		}

		String memberDomain, groupName;
		Usuario usuario = usuarioDao.cargar(file);
		groupName = request.params("groupName");
		Grupo grupo = usuario.getSingleGrupoByName(groupName);

		memberDomain = request.params("miembro");
		Peer miembro = usuarioDao.getPeer(memberDomain, grupo);
		
	
		if(!votacionesDao.exist(grupo.getId(),miembro,votacionesFile)){
			grupos.solicitarBajaMiembro(grupo, miembro);
			model.put("solicitar",true);
		}else{
			model.put("solicitudExistente",true);
		}
		

		model.put("user", usuario);
		model.put("groupName",grupo.getNombre());
		model.put("group", grupo);
		return ViewUtil.render(request, model, PathUtil.Template.VIEW_GROUP_MEMBER);
	};
	

	public static Route getVotaciones = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("Ver Votaciones");
		Usuario usuario = usuarioDao.cargar(file);
		Votaciones votacionesPendientes;
		Votaciones votaciones;
		
	
		votacionesPendientes = set(votacionesPendientesFile);
		votaciones = set(votacionesFile);
		
		
		model.put("user", usuario);
		model.put("currentUser", true);
		model.put("solicitudes",votaciones);
		model.put("votaciones", votacionesPendientes);

		return ViewUtil.render(request, model, PathUtil.Template.VER_VOTACIONES);
	};

	private static Votaciones set(File file) throws JAXBException{
		if(file.exists()){
			return votacionesDao.cargar(file);
		}
		return new Votaciones();
	}
	
	public static Route votarBaja = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();
		System.out.println("Ver Votaciones");
		Usuario usuario = usuarioDao.cargar(file);
		Votaciones votacionesPendientes;
		Votaciones votaciones;
		
		String voto = request.params("voto");
		
		String votacionId = request.params("id");
		
		Votacion votacion = votacionesDao.getVotacion(votacionId, votacionesPendientesFile);
		
		if(!grupos.isInit()){
			grupos.init();
		}
		
		if(voto.equals("aceptar")){
			grupos.aceptarBajaMiembro(votacion);
		}else{
			grupos.rechazarBajaMiembro(votacion);
		}
		
		votacionesDao.eliminarVotacion(votacion, votacionesPendientesFile);
		votacionesPendientes = set(votacionesPendientesFile);
		votaciones = set(votacionesFile);
		
		model.put("user", usuario);
		model.put("currentUser", true);
		model.put("solicitudes",votaciones);
		model.put("votaciones", votacionesPendientes);
        model.put("votacionExitosa", true);
		
		return ViewUtil.render(request, model, PathUtil.Template.VER_VOTACIONES);
	};
	

	public static Route getAllMenssages = (Request request, Response response) -> { 
	    LoginController.ensureUserIsLoggedIn(request, response); 
	    Map<String, Object> model = new HashMap<>(); 
	    Usuario usuario = usuarioDao.cargar(file); 
	    MensajesPendientes mp; 
	 
	    mensajesDao.setFile(mensajesFile);
	    mp = mensajesDao.cargar(); 
	    
	    Collections.sort(mp.getMensaje());
	    Collections.reverse(mp.getMensaje());
	    model.put("user", usuario); 
	    model.put("mensajesPendientes", mp); 
	    return ViewUtil.render(request, model, PathUtil.Template.VIEW_ALL_MESSAGES); 
	 
	  }; 
	 
	  public static Route eliminarMensaje = (Request request, Response response) -> { 
	 
	    Map<String, Object> model = new HashMap<>(); 
	    System.out.println("Eliminar Mensaje"); 
	    Usuario usuario = usuarioDao.cargar(file); 
	 
	    String mensajeId = request.params("mensajeId"); 
	     
	    mensajesDao.setFile(mensajesFile); 
	    Mensaje eliminado = mensajesDao.eliminarMensajeId(mensajeId); 
	    if(eliminado == null){ 
	       model.put("eliminacionFallida", true);   
	       request.session().removeAttribute("mensajesPendientes"); 
	    } 
	    else model.put("eliminacionExitosa", true); 
	   
	     
	    request.session().attribute("mensajesPendientes", mensajesDao.cargar(mensajesFile)); 
	       
	     
	    request.session().attribute("user", usuario); 
	     
	     
	    response.redirect(PathUtil.Web.VIEW_ALL_MESSAGES); 
	   
	    return null; 
	  }; 
	
}
