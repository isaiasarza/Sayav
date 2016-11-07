package SAYAV2.SAYAV2.service;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import SAYAV2.SAYAV2.Utils.Filters;
import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Notification;



public class Application {
	
	public static Notification createNotificacion(String registrationId){
		Notification n = new Notification();
		
		n.createData("Esto es una prueba", "Esto es una prueba");
		n.addId(registrationId);
		return n;
	}

	public static void main(String[] args) {
		
		Notification notificacion = createNotificacion("");
		port(8080);
		staticFiles.location("/public");
		staticFiles.expireTime(600L);

		//PostGCM.post(apiKey, notificacion);
		// Set up before-filters (called before each get/post)
        before("*",                  Filters.addTrailingSlashes);
        before("*",                  Filters.handleLocaleChange);
        
        
	
        //REST de registro de usuario
		get(PathUtil.Web.REGISTRATION,RegistrationController.servicioPaginaRegistrar);
		post(PathUtil.Web.REGISTRATION,RegistrationController.registrarNuevoUsuario);
		
		//REST de login de usuario
		get(PathUtil.Web.LOGIN,LoginController.serveLoginPage);	
		post(PathUtil.Web.LOGIN,LoginController.handleLoginPost); 
		post(PathUtil.Web.LOGOUT,    LoginController.handleLogoutPost);
		
		post(PathUtil.Web.ALARM, AlarmController.enableAlarmPost);
		
		post(PathUtil.Web.PANIC_BUTTON, AlarmController.panicButton);
		
		get(PathUtil.Web.MENU, UsuarioController.viewUserData);
		

		get(PathUtil.Web.UPDATE_U, UsuarioController.update);
		post(PathUtil.Web.UPDATE_U, UsuarioController.showUpdate);

		get(PathUtil.Web.UPDATE_USER, UsuarioController.viewUpdateUser);
		post(PathUtil.Web.UPDATE_USER, UsuarioController.updateUser);
		
		get(PathUtil.Web.NOTIFICATION_PUSH, (req, res) -> "Get Token");
		post(PathUtil.Web.NOTIFICATION_PUSH, FirebaseCloudMessageController.pushNotification);

//		REST de Firebasen Cloud Messaging
		get(PathUtil.Web.NOTIFICATION_TOKEN, (req, res) -> "Get Token");
		post(PathUtil.Web.NOTIFICATION_TOKEN, FirebaseCloudMessageController.postNewToken);

		get("*",                     ViewUtil.notFound);
        //Set up after-filters (called after each get/post)
        after("*",                  Filters.addGzipHeader);
		
	}
}
