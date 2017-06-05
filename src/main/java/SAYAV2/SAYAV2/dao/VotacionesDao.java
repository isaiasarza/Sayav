package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.notificacion.Votacion;
import SAYAV2.SAYAV2.notificacion.Votaciones;

public class VotacionesDao extends GenericDao<Votaciones> {
	
	private static VotacionesDao votacionesDao;
	
	private VotacionesDao() {
		super();
		this.e = new Votaciones();
		this.file = new File("votacion");
	}
	
	public static VotacionesDao getInstance(){
		if(votacionesDao == null){
			votacionesDao = new VotacionesDao();
		}
		return votacionesDao;
	}
	
	public boolean eliminarVotacion(Votacion votacion) throws JAXBException{
		Votaciones votaciones = this.cargar(file);
		Iterator<Votacion> iterator = votaciones.getVotaciones().iterator();
		while(iterator.hasNext()){
			if(iterator.next().getId().equals(votacion.getId())){
				iterator.remove();
				this.guardar(votaciones, file);
				return true;
			}
		}
		return false;
	}

}
