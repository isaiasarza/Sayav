package SAYAV2.SAYAV2.service;

import java.util.HashMap;
import java.util.Map;

import SAYAV2.SAYAV2.Utils.PathUtil;
import SAYAV2.SAYAV2.Utils.ViewUtil;
import SAYAV2.SAYAV2.model.Prueba;
import spark.Request;
import spark.Response;
import spark.Route;

public class PruebaController {

	public static Route pruebaVelocityEngine = (Request request, Response response) ->{
		Map<String, Object> model = new HashMap<>();
		Prueba prueba = new Prueba(); 
		prueba.setTitulo("Prueba");
		prueba.setSubtitulo("Esto es una prueba");
		
		model.put("prueba", prueba);
        return ViewUtil.render(request, model, PathUtil.Template.PRUEBA);
	};
}
