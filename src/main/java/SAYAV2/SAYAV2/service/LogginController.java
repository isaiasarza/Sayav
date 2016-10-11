package SAYAV2.SAYAV2.service;

import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.RequestUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogginController {
	
	 public static Route serveLoginPage = (Request request, Response response) -> {
	        Map<String, Object> model = new HashMap<>();
	        model.put("loggedOut", RequestUtil.removeSessionAttrLoggedOut(request));
	        model.put("loginRedirect", RequestUtil.removeSessionAttrLoginRedirect(request));
	        return ViewUtil.render(request, model, PathUtil.Template.LOGIN);
	    };
//
	public static Route handleLoginPost = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();
		System.out.println("Autentificando usuario");

		if (!UsuarioController.authenticate(RequestUtil.getQueryEmail(request),
				RequestUtil.getQueryPassword(request), RequestUtil.getQueryName(request), 
				RequestUtil.getQueryLastName(request))) {
			System.out.println("Incorrect data");
			model.put("authenticationFailed", true);
			return ViewUtil.render(request, model, PathUtil.Template.LOGIN);
		}
		System.out.println("The user has been correctly logged");

		model.put("authenticationSucceeded", true);
		request.session().attribute("currentUser", 
				RequestUtil.getQueryName(request) + " " + RequestUtil.getQueryLastName(request));
		if (RequestUtil.getQueryLoginRedirect(request) != null) {
			response.redirect(RequestUtil.getQueryLoginRedirect(request));
		}
		return ViewUtil.render(request, model, PathUtil.Template.LOGIN);
	};
	
	public static Route handleLogoutPost = (Request request, Response response) -> {
        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        response.redirect(PathUtil.Web.LOGIN);
        return null;
    };
    
    public static void ensureUserIsLoggedIn(Request request, Response response) {
        if (request.session().attribute("currentUser") == null) {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect(PathUtil.Web.LOGIN);
        }
    };

	
}
