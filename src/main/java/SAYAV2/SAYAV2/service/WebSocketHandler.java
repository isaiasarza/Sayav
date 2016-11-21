package SAYAV2.SAYAV2.service;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;

@WebSocket
public class WebSocketHandler {

	private String sender, msg;
	private UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private File file = new File("SAYAV");

	@OnWebSocketConnect
	public void onConnect(Session user) throws Exception {

		Usuario usuario;

		usuario = usuarioDao.cargar(file);
	
		for (Grupo g: usuario.getGrupos())
			for (Peer p : g.getPeers()) {
				System.out.println(p.getDireccion());
				PeerService.getUsernameMap().put(user, p.getDireccion());
				PeerService.broadcastMessage(sender = "Server", msg);
			}
	}

	@OnWebSocketClose
	public void onClose(Session user, int statusCode, String reason) throws JAXBException {

		Usuario usuario;

		usuario = usuarioDao.cargar(file);


		for (Grupo g: usuario.getGrupos())
			for (Peer p : g.getPeers()) {
				String username = PeerService.getUsernameMap().get(user);
				PeerService.getUsernameMap().remove(user);
				PeerService.broadcastMessage(sender = "Server", msg);
			}
	}

	@OnWebSocketMessage
	public void onMessage(Session user, String message) throws JAXBException {

		Usuario usuario;

		usuario = usuarioDao.cargar(file);
		
		for (Grupo g: usuario.getGrupos())
			for (Peer p : g.getPeers()) {
				PeerService.broadcastMessage(p.getDireccion(), msg = message);
				System.out.println(sender);
			}
		
	}

}
