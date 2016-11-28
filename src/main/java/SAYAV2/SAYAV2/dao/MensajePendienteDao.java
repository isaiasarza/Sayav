package SAYAV2.SAYAV2.dao;

import SAYAV2.SAYAV2.model.MensajesPendientes;

public class MensajePendienteDao extends GenericDao<MensajesPendientes> {

	private static MensajePendienteDao mensajePendienteDao;

	public MensajePendienteDao() {
		// TODO Auto-generated constructor stub{
		super();
		this.e = new MensajesPendientes();
	}
	
	
	
	public static MensajePendienteDao getInstance(){
		if(mensajePendienteDao == null){
			mensajePendienteDao = new MensajePendienteDao();
		}
		return mensajePendienteDao;
	}
	
	
}
