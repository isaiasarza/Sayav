package SAYAV2.SAYAV2.dao;

import org.mindrot.jbcrypt.BCrypt;

import SAYAV2.SAYAV2.model.Usuario;

public class UsuarioDao extends GenericDao{
	
	

	public UsuarioDao() {
		super();
		
	}

	public Usuario getUsuario(String email){
		return null;
	}
	
	public static String generarContraseña(Usuario usuario){
		String newSalt = BCrypt.gensalt();
		usuario.setSalt(newSalt);
		return BCrypt.hashpw(usuario.getContraseña(),newSalt);
	}
}
