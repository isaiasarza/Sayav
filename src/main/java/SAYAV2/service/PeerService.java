package SAYAV2.service;

import static j2html.TagCreator.article;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import SAYAV2.Utils.FileUtils;
import SAYAV2.dao.UsuarioDao;
import SAYAV2.model.Grupo;
import SAYAV2.model.Peer;
import SAYAV2.model.Usuario;


public class PeerService {

    
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static File file = new File(FileUtils.getUsuarioFile());
	
	// this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
    static Map<Session, String> usernameMap = new ConcurrentHashMap<>();
    static int nextUserNumber = 1; //Assign to username for next connecting user

	
	
	//Sends a message from one user to all users, along with a list of current usernames
    public static void broadcastMessage(String sender, String message) {
    		
        usernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
            try {
                session.getRemote().sendString(String.valueOf(new JSONObject()
                    .put("userMessage", createHtmlMessageFromSender(sender, message))
                    .put("userlist", usernameMap.values())
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });	
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    public static String createHtmlMessageFromSender(String sender, String message) {
        return article().with(
                b(sender + ":"),
                p(message),
                span().withClass("timestamp").withText(new SimpleDateFormat("HH:mm:ss").format(new Date()))
        ).render();
    }

	public static Map<Session, String> getUsernameMap() {
		return usernameMap;
	}

	public static void setUsernameMap(Map<Session, String> userUsernameMap) {
		PeerService.usernameMap = userUsernameMap;
	}

	public static int getNextUserNumber() {
		return nextUserNumber;
	}

	public static void setNextUserNumber(int nextUserNumber) {
		PeerService.nextUserNumber = nextUserNumber;
	}

    
	public static Peer buscarPeer(String direccion) throws JAXBException{
		
		Usuario usuario = usuarioDao.cargar(file);

		for (Grupo g : usuario.getGrupos())
			for (Peer p : g.getPeers()) {
		         if(p.getDireccion().equals(direccion))
		        	 return p;
			}
		return null;
	}
	
	
	
}
