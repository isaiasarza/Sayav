package SAYAV2.SAYAV2.service;


import static spark.Spark.get;

import static spark.Spark.port;
import static spark.Spark.post;


import static spark.Spark.staticFiles;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;





public class Application {
	
	
	
	public static void main(String[] args) {
	
		port(29100);

		staticFiles.location("/public");
		staticFiles.expireTime(600L);

		// Set up before-filters (called before each get/post)
//        before("*",                  Filters.addTrailingSlashes);
//        before("*",                  Filters.handleLocaleChange);
        
	
        
        //REST de registro de usuario
		get(PathUtil.Web.REGISTRATION,RegistrationController.servicioPaginaRegistrar);
		post(PathUtil.Web.REGISTRATION,RegistrationController.registrarNuevoUsuario);
		
		//REST de login de usuario
		get(PathUtil.Web.LOGIN,LoginController.serveLoginPage);	
		post(PathUtil.Web.LOGIN,LoginController.handleLoginPost); 
		post(PathUtil.Web.LOGOUT,    LoginController.handleLogoutPost);
		
//		POST habilitar/deshabilitar alarma
		post(PathUtil.Web.ALARM, AlarmController.enableAlarmPost);
//		POST Botón de pánico
//		post(PathUtil.Web.PANIC_BUTTON, AlarmController.panicButton);
//		
//		//REST de dispositivos moviles
//		get(PathUtil.Web.DISPOSITIVO,DispositivoController.dispositivoVelocityEngine);
//		post(PathUtil.Web.DISPOSITIVO,DispositivoController.nuevoDispositivo);
//		post(PathUtil.Web.ELIMINAR_DISPOSITIVO,DispositivoController.eliminarDispositivo);
//		
		//REST de sectores
		get(PathUtil.Web.SECTOR,SectorController.sectorVelocityEngine);
		post(PathUtil.Web.SECTOR,SectorController.numeroSectores); 
		post(PathUtil.Web.RENOMBRAR_SECTOR,SectorController.renombrarSector); 
		post(PathUtil.Web.CAMBIAR_ESTADO,SectorController.cambiarEstado);
		
		//		REST Actualizar Usuario
		get(PathUtil.Web.MENU, UsuarioController.viewUserData);
		get(PathUtil.Web.UPDATE_U, UsuarioController.update);
		post(PathUtil.Web.UPDATE_U, UsuarioController.showUpdate);
		get(PathUtil.Web.UPDATE_USER, UsuarioController.viewUpdateUser);
		post(PathUtil.Web.UPDATE_USER, UsuarioController.updateUser);
		
		

		//		Rest Crear GRupo
		get(PathUtil.Web.NEW_GROUP,GroupController.getNewGroup);
		post(PathUtil.Web.NEW_GROUP, GroupController.postNewGroup);
		
//		Rest Listado Grupos
		get(PathUtil.Web.VIEW_ALL_GROUPS,GroupController.getAllGroups);

//		Rest Añadir Miembro a grupo
		
		get(PathUtil.Web.NEW_GROUP_MEMBER,GroupController.getNewGroupMember);
		post(PathUtil.Web.NEW_GROUP_MEMBER,GroupController.postNewGroupMember);

		get(PathUtil.Web.VIEW_GROUP_MEMBER, GroupController.getViewMembers);
		
		get(PathUtil.Web.LEAVE_GROUP, GroupController.leaveGroup);
		
//		//		REST Notificaciones Push
//		get(PathUtil.Web.NOTIFICATION_PUSH, (req, res) -> "Get Token");
//		post(PathUtil.Web.NOTIFICATION_PUSH, FirebaseCloudMessageController.pushNotification);
//		
//		
//		//		REST Nuevo Token Firebase Cloud Messaging
//		get(PathUtil.Web.NOTIFICATION_TOKEN, (req, res) -> "Get Token");
//		post(PathUtil.Web.NOTIFICATION_TOKEN, FirebaseCloudMessageController.postNewToken);

//		get(PathUtil.Web.GRUOP_NOTIFICATION, GrupoController.getNotificar);
//		post(PathUtil.Web.GRUOP_NOTIFICATION,  GrupoController.notificar);
//		
//		get(PathUtil.Web.VIEW_ALL_MESSAGES, MensajesPendientesController.getAllMenssages);
		
		get("*",                     LoginController.serveLoginPage);
        //Set up after-filters (called after each get/post)
//        after("*",                  Filters.addGzipHeader);
		
		 
		
	}
}
