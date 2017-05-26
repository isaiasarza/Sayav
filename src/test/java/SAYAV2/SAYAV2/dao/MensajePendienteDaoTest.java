package SAYAV2.SAYAV2.dao;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.MensajesPendientes;

public class MensajePendienteDaoTest {
	
	MensajePendienteDao mensajePendienteDao;
	File file;
	MensajesPendientes mensajes;
	@Before
	public void setUp() throws Exception {
		file = new File("mensajes_test2");
		mensajePendienteDao = MensajePendienteDao.getInstance();
		mensajes = new MensajesPendientes();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Mensaje m1 = new Mensaje();
		Mensaje m2 = new Mensaje();
		m1.setDescripcion("MensajeTest1");
		m2.setDescripcion("MensajeTest2");
		mensajes.addMensaje(m1);
		mensajes.addMensaje(m2);
		mensajePendienteDao.guardar(mensajes, file);
	}

}
