package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.notificacion.Votacion;
import SAYAV2.SAYAV2.notificacion.Votaciones;

public class VotacionesDao extends GenericDao<Votaciones> {

	private static VotacionesDao votacionesDao;

	private VotacionesDao() {
		super();
		this.e = new Votaciones();
		// this.file = new File("votacion");

	}

	public static VotacionesDao getInstance() {
		if (votacionesDao == null) {
			votacionesDao = new VotacionesDao();
		}
		return votacionesDao;
	}

	public Votaciones eliminarVotacion(Votacion votacion, String ruta) throws JAXBException, IOException {
		Votaciones votaciones = this.cargar(ruta);
		Iterator<Votacion> iterator = votaciones.getVotaciones().iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getId().equals(votacion.getId())) {
				iterator.remove();
				this.guardar(votaciones, ruta);
				return this.cargar(ruta);
			}
		}
		return votaciones;
	}

	/*
	 * public Votaciones eliminarVotacion(Votacion votacion, File file) throws
	 * JAXBException { Votaciones votaciones = this.cargar(file); Iterator<Votacion>
	 * iterator = votaciones.getVotaciones().iterator(); while (iterator.hasNext())
	 * { if (iterator.next().getId().equals(votacion.getId())) { iterator.remove();
	 * this.guardar(votaciones, file); return this.cargar(file); } } return
	 * votaciones; }
	 */

	public Votaciones agregarVotacion(Votacion votacion, Votaciones votaciones, File votacionesPendientesFile)
			throws JAXBException {
		if (!votaciones.addVotacion(votacion)) {
			return null;
		}
		this.guardar(votaciones, votacionesPendientesFile);
		votaciones.getVotaciones().clear();
		votaciones = this.cargar(votacionesPendientesFile);
		return votaciones;
	}

	public Votaciones agregarVotacion(Votacion votacion, Votaciones votaciones, String votacionesPendientesFile)
			throws JAXBException, IOException {
		if (!votaciones.addVotacion(votacion)) {
			return null;
		}

		this.guardar(votaciones, votacionesPendientesFile);
		votaciones.getVotaciones().clear();
		votaciones = this.cargar(votacionesPendientesFile);

		return votaciones;
	}


	public Votaciones agregarVotacion(Votacion votacion, File votacionesPendientesFile) {
		Votaciones votaciones;
		try {
			votaciones = this.cargar(votacionesPendientesFile);
			if (votaciones.getVotaciones().contains(votacion)) {
				return null;
			}
			votaciones.addVotacion(votacion);
			this.guardar(votaciones, votacionesPendientesFile);
			votaciones.getVotaciones().clear();
			votaciones = this.cargar(votacionesPendientesFile);
			return votaciones;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		votaciones = new Votaciones();
		votaciones.addVotacion(votacion);
		this.guardar(votaciones, votacionesPendientesFile);
		return votaciones;

	}

	/*
	 * public boolean exist(String grupoId, Peer miembro, File file) { Votaciones
	 * votaciones;
	 * 
	 * try { votaciones = cargar(file); for (Votacion v :
	 * votaciones.getVotaciones()) { if (v.getGrupoId().equals(grupoId) &&
	 * v.getMiembro().getDireccion().equals(miembro.getDireccion())) { return true;
	 * } } } catch (JAXBException e) { e.printStackTrace(); }
	 * 
	 * return false; }
	 */

	/*
	 * public Votacion getVotacion(String votacionId, File file) { Votaciones
	 * votaciones; try { votaciones = this.cargar(file); for (Votacion v :
	 * votaciones.getVotaciones()) { if (v.getId().equals(votacionId)) { return v; }
	 * } } catch (JAXBException e) { e.printStackTrace(); } return null; }
	 */

	/*
	 * public boolean actualizarVotacion(Votacion votacion, File votacionesFile) {
	 * try { Votaciones votaciones = this.cargar(votacionesFile); int index =
	 * votaciones.getVotaciones().indexOf(votacion);
	 * votaciones.getVotaciones().set(index, votacion); this.guardar(votaciones,
	 * votacionesFile); return true; } catch (JAXBException e) {
	 * e.printStackTrace(); } return false; }
	 */

	public Votacion getVotacion(String votacionId, String ruta) {
		Votaciones votaciones;
		try {
			votaciones = this.cargar(ruta);
			for (Votacion v : votaciones.getVotaciones()) {
				if (v.getId().equals(votacionId)) {
					return v;
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean exist(String grupoId, Peer miembro, String ruta) {
		Votaciones votaciones;

		try {
			votaciones = cargar(ruta);
			for (Votacion v : votaciones.getVotaciones()) {
				if (v.getGrupoId().equals(grupoId) && v.getMiembro().getDireccion().equals(miembro.getDireccion())) {
					return true;
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean actualizarVotacion(Votacion votacion, String votacionesFile) {
		try {
			Votaciones votaciones = this.cargar(votacionesFile);
			int index = votaciones.getVotaciones().indexOf(votacion);
			votaciones.getVotaciones().set(index, votacion);
			this.guardar(votaciones, votacionesFile);
			return true;
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

	public Votaciones agregarVotacion(Votacion votacion, String votacionesFile) {
		Votaciones votaciones;
		try {
			votaciones = this.cargar(votacionesFile);
			if (!votaciones.addVotacion(votacion)) {
				return null;
			}
			this.guardar(votaciones, votacionesFile);
			//votaciones.getVotaciones().clear();
			return votaciones;
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*votaciones = new Votaciones();
		votaciones.addVotacion(votacion);
		try {
			this.guardar(votaciones, votacionesFile);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return null;
	}

}
