package SAYAV2.SAYAV2.dao;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.model.Notificacion;
import SAYAV2.SAYAV2.model.Notificaciones;

public class NotificacionesDao extends GenericDao<Notificaciones> {
	private static NotificacionesDao notificacionesDao;
	private NotificacionesDao() {
		super();
		this.e = new Notificaciones();
	}
	
	
	
	public static NotificacionesDao getInstance(){
		if(notificacionesDao == null){
			notificacionesDao = new NotificacionesDao();
		}
		return notificacionesDao;
	}



	public void agregarNotificacion(Notificacion notificacion) throws JAXBException {
		Notificaciones notificaciones = notificacionesDao.cargar(file);
		notificaciones.getNotificacion().add(notificacion);
		notificacionesDao.guardar(notificaciones, file);
	}

}
