package SAYAV2.SAYAV2.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.Peer;

public class MensajePendienteDaoTest {
	
	MensajePendienteDao mensajePendienteDao;
	File file;
	MensajesPendientes mensajes;
	@Before
	public void setUp() throws Exception {
		file = new File("Mensajes_test");
		mensajePendienteDao = MensajePendienteDao.getInstance();
		mensajePendienteDao.setFile(file);
		mensajes = mensajePendienteDao.cargar();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Mensaje m1 = new Mensaje();
		Mensaje m2 = new Mensaje();
		m2.getFechaCreacion().setTime(m2.getFechaCreacion().getTime() - TimeUnit.HOURS.toMillis(2));
		m1.setDescripcion("Mensaje Test 3");
		m1.setDetalle("Esto es la quinta prueba de JUnit:Espero que funcione:Si anda apruebo:");
		m1.setOrigen(new Peer("isaarza.ddns.net", 3200));
		m1.setDestino(new Peer("manuabruzzo.ddns.net", 3200));

		m2.setDescripcion("Mensaje Test 4");
		m2.setDetalle("Esto es la sexta prueba de JUnit:Espero que funcione:Si anda apruebo:");
		m2.setOrigen(new Peer("isaarza.ddns.net", 3200));
		m2.setDestino(new Peer("lucasboba.ddns.net", 3200));

		m1.setEstado(EstadoUtils.Estado.CONFIRMADO);
		m2.setEstado(EstadoUtils.Estado.PENDIENTE);
		assertTrue(mensajes.addMensaje(m1));
		assertFalse(mensajes.addMensaje(m1));
		assertTrue(mensajes.addMensaje(m2));
		mensajePendienteDao.guardar(mensajes, file);
	}

}
