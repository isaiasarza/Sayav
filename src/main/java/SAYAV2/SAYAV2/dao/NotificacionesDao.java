package SAYAV2.SAYAV2.dao;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.Utils.FileUtils;
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



	public synchronized void agregarNotificacion(Notificacion notificacion) {
		Notificaciones notificaciones;
		try {
			notificaciones = notificacionesDao.cargar(FileUtils.NOTIFICACIONES_FILE);		
		} catch (JAXBException e) {
			notificaciones = new Notificaciones();
		} catch (IOException e) {
			notificaciones = new Notificaciones();
			e.printStackTrace();
		}
		notificaciones.getNotificacion().add(notificacion);
		try {
			notificacionesDao.guardar(notificaciones, FileUtils.NOTIFICACIONES_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
