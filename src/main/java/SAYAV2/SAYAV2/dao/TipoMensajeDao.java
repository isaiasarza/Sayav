package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

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

	public TipoMensaje getTipo(String tipo,File file) throws JAXBException {
		TiposMensajes tipos = super.cargar(file);
		TipoMensaje tipoMensaje = tipos.getTipo(tipo);
		return tipoMensaje;
	}
	
	

	public TipoMensaje getTipo(String alerta, String tiposMensajesFile) {
		TiposMensajes tipos;
		TipoMensaje tipoMensaje = null;
		try {
			tipos = super.cargar(tiposMensajesFile);
			//System.out.println("Tipos Mensajes" + tipos.getTipos());
			tipoMensaje = tipos.getTipo(tiposMensajesFile);
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
		return tipoMensaje;
	}
	
	


}
