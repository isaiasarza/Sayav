package SAYAV2.SAYAV2.Utils;

import spark.*;

public class RequestUtil {

	
	
	public static String getQueryLocale(Request request) {
		return request.queryParams("locale");
	}

	public static String getParamIsbn(Request request) {
		return request.params("isbn");
	}

	public static String getQueryName(Request request) {
		return request.queryParams("name");
	}

	public static String getQueryLastName(Request request) {
		return request.queryParams("lastname");
	}

	public static String getQueryPhoneNumber(Request request) {
		return request.queryParams("phoneNumber");
	}

	public static String getQueryAddress(Request request) {
		return request.queryParams("address");
	}

	public static String getQuerySubdom(Request request) {
		return request.queryParams("subdom");
	}

	public static String getQueryEmail(Request request) {
		return request.queryParams("email");
	}

	public static String getQueryPassword(Request request) {
		return request.queryParams("psw1");
	}

	public static String getQueryRepeatPassword(Request request) {
		return request.queryParams("psw2");
	}

	public static String getQueryLoginRedirect(Request request) {
		return request.queryParams("loginRedirect");
	}
	
//	public static String getQueryLocale(Request request) {
//		return request.queryParams("loginRedirect");
//	}
	
	public static String getQueryMenuRedirect(Request request) {
		return request.queryParams("menuRedirect");
	}

	public static String getSessionLocale(Request request) {
		return request.session().attribute("locale");
	}

	public static String getSessionCurrentUser(Request request) {
		return request.session().attribute("currentUser");
	}

	public static boolean removeSessionAttrLoggedOut(Request request) {
		Object loggedOut = request.session().attribute("loggedOut");
		request.session().removeAttribute("loggedOut");
		return loggedOut != null;
	}

	public static String removeSessionAttrLoginRedirect(Request request) {
		String loginRedirect = request.session().attribute("loginRedirect");
		request.session().removeAttribute("loginRedirect");
		return loginRedirect;
	}
	
	public static String removeSessionAttrUser(Request request) {
		String user = request.session().attribute("user");
		request.session().removeAttribute("user");
		return user;
	}
	
	public static String removeSessionAttrMenuRedirect(Request request) {
		String menuRedirect = request.session().attribute("menuRedirect");
		request.session().removeAttribute("menuRedirect");
		return menuRedirect;
	}

	public static String removeSessionAttrRegisterRedirect(Request request) {
		String loginRedirect = request.session().attribute("registrationRedirect");
		request.session().removeAttribute("registrationRedirect");
		return loginRedirect;
	}

	public static boolean clientAcceptsHtml(Request request) {
		String accept = request.headers("Accept");
		return accept != null && accept.contains("text/html");
	}

	public static boolean clientAcceptsJson(Request request) {
		String accept = request.headers("Accept");
		return accept != null && accept.contains("application/json");
	}

	public static String getQueryAlarmStatus(Request request) {
		return request.queryParams("status");
	}

}
