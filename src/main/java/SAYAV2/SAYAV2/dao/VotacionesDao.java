package SAYAV2.SAYAV2.dao;

import SAYAV2.SAYAV2.notificacion.Votaciones;

public class VotacionesDao extends GenericDao<Votaciones> {
	
	private static VotacionesDao votacionesDao;
	
	private VotacionesDao() {
		super();
		this.e = new Votaciones();
	}
	
	public static VotacionesDao getInstance(){
		if(votacionesDao == null){
			votacionesDao = new VotacionesDao();
		}
		return votacionesDao;
	}

}
