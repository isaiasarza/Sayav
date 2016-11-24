package SAYAV2.SAYAV2.Utils;

import lombok.Getter;

public class PathUtil {

	// The @Getter methods are needed in order to access
	// the variables from Velocity Templates
	public static class Web {
		@Getter
		public static final String PEER = "/peer/";
		@Getter
		public static final String PEER_VER = "/peer";
		@Getter
		public static final String PRUEBA = "/prueba/";
		@Getter
		public static final String MENU = "/menu/";
		@Getter
		public static final String INDEX = "/index/";
		@Getter
		public static final String LOGIN = "/login/";
		@Getter
		public static final String LOGOUT = "/logout/";
		@Getter
		public static final String REGISTRATION = "/registration/";
		@Getter
		public static final String BOOKS = "/books/";
		@Getter
		public static final String ONE_BOOK = "/books/:isbn/";
		@Getter
		public static final String USER_DATA = "/menu/user_data/";
		@Getter
		public static final String UPDATE_USER = "/menu/user_data/update/";
		@Getter
		public static final String UPDATE_U = "/menu/user_data/u/";
		@Getter
		public static final String ALARM = "/alarm/";
		@Getter
		public static final String PANIC_BUTTON = "/alarm/panic_button/";
		@Getter
		public static final String NOTIFICATION = "";
		@Getter
		public static final String NOTIFICATION_TOKEN = "/notification/:token/";
		@Getter
		public static final String NOTIFICATION_PUSH = "/notification/push/";
		@Getter
		public static final String GROUP = "/group/";
		@Getter
		public static final String GRUPO = "/grupo/";
		@Getter
		public static final String NEW_GROUP = "/group/new/";
		@Getter
		public static final String VIEW_ALL_GROUPS = "/group/new/";
		@Getter
		public static final String NEW_GROUP_MEMBER = "/group/new/:groupName/";
		public static final String VIEW_NEW_MEMBER = "/group/new/member/";

		public static final String VIEW_GROUP_MEMBER = "/group/view/members/:groupName/";
		public static final String VIEW_MEMBERS = "/group/view/members/";
		@Getter
		public static final String DISPOSITIVO = "/dispositivo/";
		@Getter
		public static final String ELIMINAR_DISPOSITIVO = "/dispositivo/eliminar/";
		@Getter
		public static final String SECTOR = "/sector/";
		@Getter
		public static final String RENOMBRAR_SECTOR = "/sector/renombrar/";
		@Getter
		public static final String TABLADISPOSITIVOS = "/tabladispositivos/";
		@Getter
		public static final String TABLASECTORES = "/tablasectores/";
		@Getter
		public static final String ONE_DISPOSITIVO = "/dispositivo:numero/";
		public static final String CAMBIAR_ESTADO = "/sector/:nombre/";
		public static final String GRUOP_NOTIFICATION = "/grupo/notificar/";
		public static final String LEAVE = "/grupo/abandonar/";
		public static final String LEAVE_GROUP = "/grupo/abandonar/:groupName/";
		
		public static String getPrueba() {
			return PRUEBA;
		}

		public static String getIndex() {
			return INDEX;
		}

		public static String getLogin() {
			return LOGIN;
		}

		public static String getLogout() {
			return LOGOUT;
		}

		public static String getRegistration() {
			return REGISTRATION;
		}

		public static String getBooks() {
			return BOOKS;
		}

		public static String getOneBook() {
			return ONE_BOOK;
		}

		public static String getUserData() {
			return USER_DATA;
		}

		public static String getAlarm() {
			return ALARM;
		}

		public static String getMenu() {
			return MENU;
		}

		public static String getUpdateUser() {
			return UPDATE_USER;
		}

		public static String getUpdateU() {
			return UPDATE_U;
		}

		public static String getPanicButton() {
			return PANIC_BUTTON;
		}

		public static String getNotification() {
			return NOTIFICATION;
		}

		public static String getNotificationToken() {
			return NOTIFICATION_TOKEN;
		}

		public static String getNotificationPush() {
			return NOTIFICATION_PUSH;
		}

		public static String getGroup() {
			return GROUP;
		}

		public static String getNewGroup() {
			return NEW_GROUP;
		}

		public static String getViewAllGroups() {
			return VIEW_ALL_GROUPS;
		}

		public static String getNewGroupMember() {
			return NEW_GROUP_MEMBER;
		}

		public static String getViewGroupMember() {
			return VIEW_GROUP_MEMBER;
		}

		public static String getViewMembers() {
			return VIEW_MEMBERS;
		}

		public static String getViewNewMember() {
			return VIEW_NEW_MEMBER;
		}

		public static String getDispositivo() {
			return DISPOSITIVO;
		}

		public static String getEliminarDispositivo() {
			return ELIMINAR_DISPOSITIVO;
		}

		public static String getSector() {
			return SECTOR;
		}

		public static String getRenombrarSector() {
			return RENOMBRAR_SECTOR;
		}

		public static String getTabladispositivos() {
			return TABLADISPOSITIVOS;
		}

		public static String getTablasectores() {
			return TABLASECTORES;
		}

		public static String getOneDispositivo() {
			return ONE_DISPOSITIVO;
		}

		public static String getPeer() {
			return PEER;
		}

		public static String getPeerVer() {
			return PEER_VER;
		}

		public static String getGrupo() {
			return GRUPO;
		}

		public static String getCambiarEstado() {
			return CAMBIAR_ESTADO;
		}

		public static String getGruopNotification() {
			return GRUOP_NOTIFICATION;
		}

		public static String getLeave() {
			return LEAVE;
		}

		public static String getLeaveGroup() {
			return LEAVE_GROUP;
		}
		
		

	}

	public static class Template {
		public static final String PEER = "/velocity/peer/nuevoPeer.vm/";
		public static final String PRUEBA = "/velocity/pruebas/prueba.vm/";
		public static final String MENU = "/velocity/menu/menu.vm";
		public final static String INDEX = "/velocity/index/index.vm";
		public final static String LOGIN = "/velocity/login/login.vm";
		public final static String REGISTRATION = "/velocity/login/registration.vm";
		public final static String BOOKS_ALL = "/velocity/book/all.vm";
		public static final String BOOKS_ONE = "/velocity/book/one.vm";
		public static final String USER_DATA = "/velocity/user_data/user.vm";
		public static final String UPDATE_USER = "/velocity/user_data/user_update.vm";
		public static final String ALARM = "/alarm/alarm.vm";
		public static final String NEW_GROUP = "/velocity/user_data/new_group.vm";
		public static final String NOT_FOUND = "/velocity/notFound.vm";
		public static final String VIEW_ALL_GROUPS = "/velocity/user_data/view_groups.vm";
		public static final String NEW_GROUP_MEMBER = "/velocity/user_data/new_member.vm";
		public static final String VIEW_GROUP_MEMBER = "/velocity/user_data/view_members.vm";
		public final static String DISPOSITIVO = "/velocity/dispositivo/nuevoDispositivo.vm";
		public final static String ONE_DISPOSITIVO = "/velocity/dispositivo/dispositivo.vm";
		public final static String SECTOR = "/velocity/sector/nuevoSector.vm";
		public static final String TABLADISPOSITIVOS = "/velocity/dispositivo/tablaDispositivos.vm";
		public static final String TABLASECTORES = "/velocity/sensor/tablasectores.vm";
		public static final String GRUPO = "/velocity/grupo/nuevoGrupo.vm/";
		
	}

}
