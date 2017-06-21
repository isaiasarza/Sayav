package SAYAV2.service;

import java.util.HashMap;
import java.util.Map;

import SAYAV2.Utils.PathUtil;
import SAYAV2.Utils.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

public class MenuController {
	public static Route showMenu = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();
		model.put("user", UsuarioController.getCurrentUser());
		//model.put("loginRedirect", RequestUtil.removeSessionAttrLoginRedirect(request));
		return ViewUtil.render(request, model, PathUtil.Template.LOGIN);
	};
}
