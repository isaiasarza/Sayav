package SAYAV2.SAYAV2.dao;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.TipoMensaje;
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

	public void setTipo(Mensaje mensaje, String tipo) throws JAXBException {
		TiposMensajes tipos = tipoMensajeDao.cargar(file);
		TipoMensaje tipoMensaje = tipos.getTipo(tipo);
		mensaje.setTipoMensaje(tipoMensaje);		
	}
	
	


}
