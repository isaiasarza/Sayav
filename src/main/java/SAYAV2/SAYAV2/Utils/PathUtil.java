package SAYAV2.SAYAV2.Utils;

import lombok.Getter;

public class PathUtil {

	// The @Getter methods are needed in order to access
	// the variables from Velocity Templates
	public static class Web {
		@Getter
		public static final String PRUEBA = "/prueba/";
		@Getter
		public static final String INDEX = "/index/";
		@Getter
		public static final String LOGIN = "/login/";
		@Getter
		public static final String REGISTRATION = "/registration/";
		@Getter
		public static final String LOGOUT = "/logout/";
		@Getter
		public static final String BOOKS = "/books/";
		@Getter
		public static final String ONE_BOOK = "/books/:isbn/";
	}

	public static class Template {
		public static final String PRUEBA = "/velocity/pruebas/prueba.vm/";
		public final static String INDEX = "/velocity/index/index.vm";
		public final static String LOGIN = "/velocity/login/login.vm";
		public final static String REGISTRATION = "/velocity/login/registration.vm";
		public final static String BOOKS_ALL = "/velocity/book/all.vm";
		public static final String BOOKS_ONE = "/velocity/book/one.vm";
		public static final String NOT_FOUND = "/velocity/notFound.vm";
	}

}
