package SAYAV2.SAYAV2.dao;

import java.util.Iterator;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.mensajeria.Mensaje;
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



	public boolean eliminarMensaje(Mensaje msg) throws JAXBException {
		MensajesPendientes mensajes = this.cargar(file);
		Iterator<Mensaje> iterator = mensajes.getMensaje().iterator();
		while(iterator.hasNext()){
			if(iterator.next().equals(msg)){
				iterator.remove();
				return true;
			}
		}
		return false;
	}
	
	
}
