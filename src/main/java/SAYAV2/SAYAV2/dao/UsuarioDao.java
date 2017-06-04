package SAYAV2.SAYAV2.dao;

import java.io.File;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;

public class UsuarioDao extends GenericDao<Usuario>{
	private static UsuarioDao usuarioDao;
	

	private UsuarioDao() {
		super();
		this.e = new Usuario();
	}
	
	
	
	public static UsuarioDao getInstance(){
		if(usuarioDao == null){
			usuarioDao = new UsuarioDao();
		}
		return usuarioDao;
	}

	public Usuario getUsuario(String email){
		return null;
	}
	
	public void actualizar(Usuario usuario, File file) throws JAXBException{
		
		usuarioDao.guardar(usuario, file);
		
	}
	
	public Peer getPeer(String dirección, Grupo grupo){
		for(Peer p: grupo.getPeers()){
			if(p.getDireccion().equals(dirección)){
				return p;
			}
		}
		return null;
	}
	
	
	
}
