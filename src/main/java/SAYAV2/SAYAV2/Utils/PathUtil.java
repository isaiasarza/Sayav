package SAYAV2.SAYAV2.Utils;

import lombok.Getter;

public class PathUtil {

	// The @Getter methods are needed in order to access
	// the variables from Velocity Templates
	public static class Web {
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
		public static final String UPDATE_USER = "/menu/user_data/update";
		@Getter
		public static final String UPDATE_U = "/menu/user_data/u/";
		@Getter
		public static final String ALARM = "/alarm/";
		
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
		
		
			
	}

	public static class Template {
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
		public static final String NOT_FOUND = "/velocity/notFound.vm";

	}

}
