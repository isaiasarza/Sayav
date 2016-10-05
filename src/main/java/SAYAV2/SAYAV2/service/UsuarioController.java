package SAYAV2.SAYAV2.service;

import java.io.File;

import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Usuario;

public class UsuarioController {
	

	private static UsuarioDao usuarioDao;
	
	
	public UsuarioController() {
		super();
		usuarioDao = new UsuarioDao();
	}
	


	public static String registrarUsuario(Usuario usuario) {
		if(usuarioDao == null)
			usuarioDao = new UsuarioDao();
		File file;
		System.out.println("Registrando: " + usuario);
		file = new File(usuario.getNombre() + " " + usuario.getApellido());
		file.setWritable(true);
		file.setReadable(true);
		String hashedPassword = UsuarioDao.generarContraseña(usuario);
		usuario.setContraseña(hashedPassword);
		usuarioDao.guardar(usuario, file);
		System.out.println("Usuario guardado");
		return "registrationSucceeded";
	}



	public static boolean authenticate(String queryEmail, String queryPassword, String queryName,
			String queryLastName) {
		// TODO Auto-generated method stub
		return false;
	}



	


	
}
