package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;

public class UsuarioDao extends GenericDao<Usuario> {
	private static UsuarioDao usuarioDao;

	private UsuarioDao() {
		super();
		this.e = new Usuario();
	}

	public static UsuarioDao getInstance() {
		if (usuarioDao == null) {
			usuarioDao = new UsuarioDao();
		}
		return usuarioDao;
	}

	public Usuario getUsuario(String email) {
		return null;
	}

	@Deprecated
	public void actualizar(Usuario usuario, File file) throws JAXBException {

		usuarioDao.guardar(usuario, file);

	}

	public Peer getPeer(String dirección, Grupo grupo) {
		for (Peer p : grupo.getPeers()) {
			if (p.getDireccion().equals(dirección)) {
				return p;
			}
		}
		return null;
	}

	public boolean eliminarMiembro(Grupo grupo,Peer miembro) throws JAXBException{
		Usuario usuario = this.cargar(file);
		Iterator<Peer> iterator = grupo.getPeers().iterator();	
		while(iterator.hasNext()){
			if( iterator.next().equals(miembro)){
				iterator.remove();
				this.guardar(usuario, file);
				return true;
			}
		}
		return false;
	}
	
	public boolean agregarMiembro(Grupo grupo, Peer miembro){
		return false;
	}

	public boolean agregarGrupo(Grupo grupo) throws JAXBException {
		Usuario usuario = this.cargar(file);
		if(usuario.addGrupo(grupo)){
			this.guardar(usuario, file);
			return true;
		}
		return false;
	}

	public Grupo getGrupo(int i) throws JAXBException {
		Usuario usuario = this.cargar(file);
		if(usuario.getGrupos().size() > i){
			return usuario.getGrupos().get(i);
		}
		return null;
	}

	public String getSubdominio() {
		try {
			Usuario usuario = usuarioDao.cargar(file);
			return usuario.getSubdominio();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
