package SAYAV2.SAYAV2.service;


import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.databind.ObjectMapper;

import SAYAV2.SAYAV2.Utils.FileUtils;
import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.bussines.ControllerMQTT;
import SAYAV2.SAYAV2.dao.ConfiguratorDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.Mensajeria;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.model.Configurator;


public class Application {
	static ObjectMapper mapper; 
	static Configurator config;
	static ConfiguratorDao configDao;
	//static File c;
	static File sayav = new File(FileUtils.getUsuarioFile());
	static Mensajeria mensajeria;
	static ControllerMQTT controllerMqtt;

	public static void config() throws JAXBException, IOException {
	//	controllerMqtt = ControllerMQTT.getInstance();
		configDao = ConfiguratorDao.getInstance();
		config = configDao.cargar(FileUtils.CONFIGURATOR_FILE);
		mapper = new ObjectMapper();
		mensajeria = MensajeriaImpl.getInstance();
		mensajeria.init();
	//	controllerMqtt = ControllerMQTT.getInstance();
	}

	public static int getPort() {
		return config.getPort();
	}

	public static void main(String[] args) {
		staticFiles.location("/resources/public");
		staticFiles.expireTime(600L);
	
		MessageChecker messageChecker = new MessageChecker();

		try {
			config();

			port(getPort());

		//	controllerMqtt.start();

			if (sayav.exists()) {
				//System.out.println(sayav.getAbsolutePath());
		//		controllerMqtt.initReceive();
			}

			messageChecker.start();

			post("/", (request,response)->{
				Mensaje mensaje = mapper.readValue(request.bodyAsBytes(), Mensaje.class);
				//System.out.println("Mensaje recibido");
				//System.out.println(mensaje);
				mensajeria.recibirMensaje(mensaje);
				return "ok";
			});


			// Set up before-filters (called before each get/post)
			// before("*", Filters.addTrailingSlashes);
			// before("*", Filters.handleLocaleChange);

			// post(PathUtil.Web.SHOW_NOTIFICATION,IndexController.mostrarNotificacion);

			// REST de registro de usuario
			get(PathUtil.Web.REGISTRATION, RegistrationController.servicioPaginaRegistrar);
			post(PathUtil.Web.REGISTRATION, RegistrationController.registrarNuevoUsuario);

			// REST de login de usuario
			get(PathUtil.Web.LOGIN, LoginController.serveLoginPage);
			post(PathUtil.Web.LOGIN, LoginController.handleLoginPost);
			post(PathUtil.Web.LOGOUT, LoginController.handleLogoutPost);

			// POST habilitar/deshabilitar alarma
			post(PathUtil.Web.ALARM, AlarmController.enableAlarmPost);
			// POST Botón de pánico
			// post(PathUtil.Web.PANIC_BUTTON, AlarmController.panicButton);
			//
			// //REST de dispositivos moviles
			get(PathUtil.Web.DISPOSITIVO, DispositivoController.dispositivoVelocityEngine);
			// post(PathUtil.Web.DISPOSITIVO,DispositivoController.nuevoDispositivo);
			post(PathUtil.Web.ELIMINAR_DISPOSITIVO, DispositivoController.eliminarDispositivo);
			//
			// REST de sectores
			get(PathUtil.Web.SECTOR, SectorController.sectorVelocityEngine);
			post(PathUtil.Web.SECTOR, SectorController.numeroSectores);
			post(PathUtil.Web.RENOMBRAR_SECTOR, SectorController.renombrarSector);
			post(PathUtil.Web.CAMBIAR_ESTADO, SectorController.cambiarEstado);

			// REST Actualizar Usuario
			get(PathUtil.Web.MENU, UsuarioController.viewUserData);
			get(PathUtil.Web.UPDATE_U, UsuarioController.update);
			post(PathUtil.Web.UPDATE_U, UsuarioController.showUpdate);
			get(PathUtil.Web.UPDATE_USER, UsuarioController.viewUpdateUser);
			post(PathUtil.Web.UPDATE_USER, UsuarioController.updateUser);

			// Rest Crear GRupo
			get(PathUtil.Web.NEW_GROUP, GroupController.getNewGroup);
			post(PathUtil.Web.NEW_GROUP, GroupController.postNewGroup);

			// Rest Listado Grupos
			get(PathUtil.Web.VIEW_ALL_GROUPS, GroupController.getAllGroups);

			// Rest Añadir Miembro a grupo

			get(PathUtil.Web.NEW_GROUP_MEMBER, GroupController.getNewGroupMember);
			post(PathUtil.Web.NEW_GROUP_MEMBER, GroupController.postNewGroupMember);

			get(PathUtil.Web.VIEW_GROUP_MEMBER, GroupController.getViewMembers);

			get(PathUtil.Web.VER_VOTACIONES, GroupController.getVotaciones);

			post(PathUtil.Web.SOLICITAR, GroupController.solicitarBaja);

			post(PathUtil.Web.VOTO, GroupController.votarBaja);

			post(PathUtil.Web.LEAVE_GROUP, GroupController.leaveGroup);

			get(PathUtil.Web.SHOW_NOTIFICATION, NotificationController.mostrarNotificacion);

			// Mensajes Pendientes
			get(PathUtil.Web.VIEW_ALL_MESSAGES, GroupController.getAllMenssages);
			get(PathUtil.Web.VIEW_ALL_MESSAGES_STATUS, GroupController.getAllMessagesByStatus);
			post(PathUtil.Web.CAMBIAR_ESTADO_MENSAJE_ZOMBIE, GroupController.postCambiarEstado);
			// post(PathUtil.Web.ELIMINAR_MENSAJE,
			// GroupController.eliminarMensaje);

			post("/pru/", IndexController.pru);
			// // REST Notificaciones Push
			// get(PathUtil.Web.NOTIFICATION_PUSH, (req, res) -> "Get Token");
			post(PathUtil.Web.NOTIFICATION_PUSH, FirebaseCloudMessageController.pushNotification);
			//
			//
			// // REST Nuevo Token Firebase Cloud Messaging
			// get(PathUtil.Web.NOTIFICATION_TOKEN, (req, res) -> "Get Token");
			post(PathUtil.Web.NOTIFICATION_TOKEN, FirebaseCloudMessageController.postNewToken);

		

			get("*", LoginController.serveLoginPage);
			// Set up after-filters (called after each get/post)
			// after("*", Filters.addGzipHeader);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
