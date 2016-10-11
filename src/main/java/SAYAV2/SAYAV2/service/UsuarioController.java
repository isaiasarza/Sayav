package SAYAV2.SAYAV2.service;

import java.io.File;

import SAYAV2.SAYAV2.Utils.PasswordUtils;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Usuario;

public class UsuarioController {
	

	private static UsuarioDao usuarioDao;
	
	
	public UsuarioController() {
		super();
		usuarioDao = new UsuarioDao();
	}
	/**
	 * 
	 * @param usuario
	 * @return the state of the registration
	 */
	public static String registrarUsuario(Usuario usuario) {
		if(usuarioDao == null)
			usuarioDao = new UsuarioDao();
		File file;
		System.out.println("Registrando: " + usuario);
		file = new File(usuario.getNombre() + " " + usuario.getApellido());
		file.setWritable(true);
		file.setReadable(true);
		String hashedPassword = PasswordUtils.generarContraseña(usuario);
		usuario.setContraseña(hashedPassword);
		usuarioDao.guardar(usuario, file);
		System.out.println("Usuario guardado");
		return "registrationSucceeded";
	}


/**
 * 
 * @param queryEmail 
 * @param queryPassword
 * @param queryName
 * @param queryLastName
 * @return
 */
	public static boolean authenticate(String queryEmail, String queryPassword, String queryName,
			String queryLastName) {
		if(usuarioDao == null)
			usuarioDao = new UsuarioDao();
		System.out.println("Autentificando Usuario");
		File file = new File(queryName + " " + queryLastName);
		System.out.println(file.toString());
		Usuario usuario,usuario1 = new Usuario();
		//leo usuario del archivo
		usuario = (Usuario) usuarioDao.cargar(usuario1, file);
		System.out.println(usuario);
		//genero contraseña hash
		
		if(usuario.getEmail().equals(queryEmail) && 
				PasswordUtils.esContraseñaValida(queryPassword, usuario.getContraseña())){
			return true;
		}
		return false;
	}
	
}
