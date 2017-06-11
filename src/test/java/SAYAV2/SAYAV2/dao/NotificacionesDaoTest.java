package SAYAV2.SAYAV2.dao;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.model.Notificacion;

public class NotificacionesDaoTest {
	NotificacionesDao notisDao;
	File notisFile;

	@Before
	public void setUp() throws Exception {
		notisFile = new File("notificaciones");
		notisDao = NotificacionesDao.getInstance();
		notisDao.setFile(notisFile);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGuardar() {
		Notificacion notificacion = new Notificacion();
		notificacion.setDescripcion("Esta es la tercer prueba");
		notificacion.setDetalle("Se esta probando a traves de un JUnit el correcto guardado de una notificacion:La idea es ver que se separe en parrafos:Espero que esto ande");
		notificacion.setTipo("Test");
		String[] split = notificacion.getDetalle().split(":");
		for(String s: split){
			System.out.println(s);
		}
		notisDao.agregarNotificacion(notificacion);
	}

}
