package SAYAV2.dao;

import javax.xml.bind.JAXBException;

import SAYAV2.model.Notificacion;
import SAYAV2.model.Notificaciones;

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



	public synchronized void agregarNotificacion(Notificacion notificacion) {
		Notificaciones notificaciones;
		try {
			notificaciones = notificacionesDao.cargar(file);		
		} catch (JAXBException e) {
			notificaciones = new Notificaciones();
		}
		notificaciones.getNotificacion().add(notificacion);
		notificacionesDao.guardar(notificaciones, file);
	}

}
