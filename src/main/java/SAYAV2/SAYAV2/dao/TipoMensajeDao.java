package SAYAV2.SAYAV2.dao;

import SAYAV2.SAYAV2.model.TiposMensajes;

public class TipoMensajeDao extends GenericDao<TiposMensajes>{
	
	private static TipoMensajeDao tipoMensajeDao;
	

	private TipoMensajeDao() {
		super();
		this.e = new TiposMensajes();
	}
	
	public static TipoMensajeDao getInstance(){
		if(tipoMensajeDao == null){
			tipoMensajeDao = new TipoMensajeDao();
		}
		return tipoMensajeDao;
	}
	
	


}
