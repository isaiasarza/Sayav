package SAYAV2.SAYAV2.Utils;

import org.mindrot.jbcrypt.BCrypt;

import SAYAV2.SAYAV2.model.Usuario;

public class PasswordUtils {
	public static String generarContraseña(Usuario usuario){
		String newSalt = BCrypt.gensalt();
		usuario.setSalt(newSalt);
		return BCrypt.hashpw(usuario.getContraseña(),newSalt);
	}
	
	public static boolean esContraseñaValida(String contraseñaCandidata,String contraseñaHash){
		return BCrypt.checkpw(contraseñaCandidata,contraseñaHash);
	}
}
