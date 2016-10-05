package SAYAV2.SAYAV2.service;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import SAYAV2.SAYAV2.Utils.PathUtil;

public class Application {

	public static void main(String[] args) {

		port(8080);
		staticFiles.location("/public");
		staticFiles.expireTime(600L);

		
		
		//REST de registro de usuario
		get(PathUtil.Web.REGISTRATION,LogginController.servicioPaginaRegistrar);
		post(PathUtil.Web.REGISTRATION,LogginController.registrarNuevoUsuario);
		
		//REST de loggin de usuario
		get(PathUtil.Web.LOGIN,LogginController.serveLoginPage);
		post(PathUtil.Web.LOGIN,LogginController.handleLoginPost);
	}
}
