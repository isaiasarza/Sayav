package SAYAV2.SAYAV2.bussines;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotEquals;


import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;

public class MensajeriaImplTest {
	
	MensajeriaImpl mensajeria;
	Mensaje mensaje;
	

	@Before
	public void setUp() throws Exception {
		mensajeria = MensajeriaImpl.getInstance();
		mensaje = mensajeria.getMensajes().getMensaje().get(7);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testProcesarMensaje() {
		fail("Not yet implemented");
	}

	@Test
	public void testPropagarMensaje() {
		fail("Not yet implemented");
	}

	@Test
	public void testGuardarMensajePendiente() {
		fail("Not yet implemented");
	}

	@Test
	public void testReenviarMensajePendiente() {
		Date fechaInicial = (Date) mensaje.getFecha().clone();
		Date fechaReenvio;
		mensajeria.reenviarMensaje(mensaje);
		fechaReenvio = mensaje.getFecha();
		
		assertNotEquals(fechaInicial.getTime(),fechaReenvio.getTime());
	}

	@Test
	public void testEnviarSolicitud() {
		System.out.println(mensaje);
		String msg = mensajeria.enviarSolicitud(mensaje);
		System.out.println(msg);	
	}

	@Test
	public void testEnviarConfirmacion() {
		
		fail("Not yet implemented");
	}

	@Test
	public void testRecibirSolicitud() {
		Mensaje msg = new Mensaje();
		msg.setOrigen("lucas.ddns.net");
		msg.setDestino("isaiasarza.ddns.net");
		msg.setDescripcion("Blablablabla");
		msg.setFecha(new Date());
		msg.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensajeria.recibirSolicitud(msg);
	}

	@Test
	public void testRecibirConfirmación() {
		fail("Not yet implemented");
	}

	@Test
	public void testEliminarMensajePendiente() {
		fail("Not yet implemented");
	}

}
