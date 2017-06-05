package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
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
	
	public Votaciones eliminarVotacion(Votacion votacion,File file) throws JAXBException{
		Votaciones votaciones = this.cargar(file);
		Iterator<Votacion> iterator = votaciones.getVotaciones().iterator();
		while(iterator.hasNext()){
			if(iterator.next().getId().equals(votacion.getId())){
				iterator.remove();
				this.guardar(votaciones, file);
				return this.cargar(file);
			}
		}
		return votaciones;
	}

	public Votaciones agregarVotacion(Votacion votacion, Votaciones votaciones,
			File votacionesPendientesFile) throws JAXBException {
		votaciones.addVotacion(votacion);
		this.guardar(votaciones, votacionesPendientesFile);
		votaciones.getVotaciones().clear();
		votaciones = this.cargar(votacionesPendientesFile);
		return votaciones;
	}

	public boolean exist(Grupo grupo, Peer miembro,File file) {
		Votaciones votaciones;
		try {
			votaciones = cargar(file);
			for(Votacion v: votaciones.getVotaciones()){
				if(v.getGrupo().getId().equals(grupo.getId()) && v.getMiembro().getDireccion().equals(miembro.getDireccion())){
					return true;
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public Votacion getVotacion(String votacionId,File file) {
		Votaciones votaciones;
		try {
			votaciones = this.cargar(file);
			for(Votacion v: votaciones.getVotaciones()){
				if(v.getId().equals(votacionId)){
					return v;
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
