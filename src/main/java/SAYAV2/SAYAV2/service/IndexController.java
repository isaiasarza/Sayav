package SAYAV2.SAYAV2.service;

import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

public class IndexController {

	public static Route servicioPaginaInicio = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();

		return ViewUtil.render(request, model, PathUtil.Template.INDEX);
	};

	public static Route pru = (Request request, Response response) -> {
		System.out.println("pru");
		return null;
	};
}
