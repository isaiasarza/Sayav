package SAYAV2.SAYAV2.service;

import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import spark.Request;
import spark.Response;
import spark.Route;

public class PeerController {
	
	
	
	public static Route peerVelocityEngine = (Request request, Response response) -> {

		LoginController.ensureUserIsLoggedIn(request, response);
		Map<String, Object> model = new HashMap<>();

	
		Peer peer = new Peer();

		
		model.put("peer", peer);
	
		System.out.println("algo");
		
		int id = Integer.valueOf(RequestUtil.getQueryGrupoSeleccionado(request));		
		
		System.out.println(id);
		
		//Grupo grupo = buscarGrupo(id);
		
		//model.put("listaPeers", grupo.getPeers());

		return ViewUtil.render(request, model, PathUtil.Template.PEER);
	};

	public static Route nuevoPeer = (Request request, Response response) -> {

		Map<String, Object> model = new HashMap<>();

		Usuario usuario;

		Peer peer = new Peer(); 
		
		peer = initPeer(request);
		
		usuario = UsuarioController.getCurrentUser();

		int id = Integer.valueOf(RequestUtil.getQueryGrupoSeleccionado(request));
		

		System.out.println(id);
		

		// Actualizo el Usuario
		UsuarioController.setCurrentUser(usuario);

		UsuarioDao.getInstance().guardar(usuario);
		model.put("peer", peer);

		return ViewUtil.render(request, model, PathUtil.Template.PEER);

	};

	private static Peer initPeer(Request request) {

		Peer peer = new Peer(RequestUtil.getQueryDireccion(request),RequestUtil.getQueryPuerto(request));
		//peer.setDireccion(RequestUtil.getQueryDireccion(request));
		//peer.setPuerto(RequestUtil.getQueryPuerto(request));
		return peer;
	}

}
