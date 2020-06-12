package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.Utils.FileUtils;
import SAYAV2.SAYAV2.model.DispositivoM;
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
	
	public Usuario cargar() throws JAXBException, IOException {
		return super.cargar(FileUtils.USUARIO_FILE);
	}
	
	public void guardar(Usuario usuario) throws JAXBException, IOException {
		super.guardar(usuario,FileUtils.USUARIO_FILE);
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

	public Peer eliminarMiembro(Grupo grupo,Peer miembro) throws JAXBException{
		Usuario usuario;
		try {
			usuario = this.cargar(FileUtils.USUARIO_FILE);
			System.out.println(grupo.getId());
			Grupo g = usuario.getSingleGrupoById(grupo.getId());
			Iterator<Peer> iterator = g.getPeers().iterator();	
			while(iterator.hasNext()){
				Peer eliminado = iterator.next();
				if(eliminado.getDireccion().equals(miembro.getDireccion())){	
					Peer removed = new Peer(eliminado.getDireccion(),eliminado.getPuerto());
					iterator.remove();
					this.guardar(usuario, FileUtils.USUARIO_FILE);
					return removed;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public boolean eliminarGrupo(Grupo grupo) throws JAXBException{
		
		//Primero limpio la lista de miembros correspondiente a ese grupo
		grupo.getPeers().clear();
	    //Elimino el grupo
		Usuario usuario;
		try {
			usuario = this.cargar(FileUtils.USUARIO_FILE);
			Iterator<Grupo> iterator = usuario.getGrupos().iterator();	
			while(iterator.hasNext()){
				if( iterator.next().getId().equals(grupo.getId())){
					iterator.remove();
					this.guardar(usuario, FileUtils.USUARIO_FILE);
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
		
	public synchronized boolean agregarMiembro(Grupo grupo, Peer miembro) throws JAXBException{
		System.out.println();
		System.out.println("9. Agregar Miembro, " + miembro.getDireccion()+ ":" + miembro.getPuerto());
		System.out.println();
		Usuario usuario;				
		try {
			usuario = this.cargar(FileUtils.USUARIO_FILE);
			if(miembro.getDireccion().equals((usuario.getSubdominio()))) {
				return false;
			}
			Grupo g = usuario.getSingleGrupoById(grupo.getId());
			if(g.addPeer(miembro.getDireccion(),miembro.getPuerto())){
				this.guardar(usuario, FileUtils.USUARIO_FILE);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public boolean agregarGrupo(Grupo grupo) throws JAXBException {
		Usuario usuario;
		try {
			usuario = this.cargar(FileUtils.USUARIO_FILE);
			if(getPeer(usuario.getSubdominio(), grupo) != null) {
				eliminarMiembro(grupo, new Peer(usuario.getSubdominio(),0));
			}
			if(usuario.addGrupo(grupo)){
				this.guardar(usuario, FileUtils.USUARIO_FILE);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public Grupo getGrupo(int i) throws JAXBException {
		Usuario usuario;
		try {
			usuario = this.cargar(FileUtils.USUARIO_FILE);
			if(usuario.getGrupos().size() > i){
				return usuario.getGrupos().get(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public String getNombreDeUsuario() {
		try {
			Usuario usuario = usuarioDao.cargar(FileUtils.USUARIO_FILE);
			return usuario.getNombreDeUsuario();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Grupo getGrupo(String id) {
		try {
			Usuario usuario = usuarioDao.cargar(FileUtils.USUARIO_FILE);
			return usuario.getSingleGrupoById(id);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getSubdominio() {
		try {
			Usuario usuario = usuarioDao.cargar(FileUtils.USUARIO_FILE);
			return usuario.getSubdominio();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	

	public boolean eliminarDispositivo(DispositivoM d, File file) {
		Iterator<DispositivoM> dispositivos;
		try {
			Usuario usuario = this.cargar(FileUtils.USUARIO_FILE);
			dispositivos = usuario.getDispositivosMoviles().iterator();
			if(d.getToken() == null || d.getToken().isEmpty()){
				while(dispositivos.hasNext()){
					DispositivoM disp = dispositivos.next();
					if(disp.getNumero().equals(d.getNumero())){
						dispositivos.remove();
						this.guardar(usuario, FileUtils.USUARIO_FILE);
						return true;
					}
				}
			}
			if(usuario.getDispositivosMoviles().remove(d)){
				this.guardar(usuario, FileUtils.USUARIO_FILE);
				return true;
			}
		} catch (JAXBException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean eliminarDispositivo(String token, File file) {
		try {
			Usuario usuario = this.cargar(FileUtils.USUARIO_FILE);
			DispositivoM d = new DispositivoM(token);
			if(usuario.getDispositivosMoviles().remove(d)){
				this.guardar(usuario, FileUtils.USUARIO_FILE);
				return true;
			}
			return false;
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean eliminarDispositivo(String token) {
		try {
			Usuario usuario = this.cargar();
			DispositivoM d = new DispositivoM(token);
			if(usuario.getDispositivosMoviles().remove(d)){
				this.guardar(usuario, FileUtils.USUARIO_FILE);
				return true;
			}
			return false;
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
