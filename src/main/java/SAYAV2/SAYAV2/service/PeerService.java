package SAYAV2.SAYAV2.service;

import static j2html.TagCreator.article;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;
import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import SAYAV2.SAYAV2.model.Mensaje;


public class PeerService {

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
    private static String createHtmlMessageFromSender(String sender, String message) {
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

	
//	public static void main(String[] args) {
//	       
//    	port(8080);
//    	staticFiles.location("/public"); //index.html is served at localhost:4567 (default port)
//        staticFiles.expireTime(600);
//        webSocket("/chat", ChatWebSocketHandler.class);
//        init();
//        
//        
//        
//        
//        
//        
//        
//    }
	

}
