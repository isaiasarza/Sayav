package SAYAV2.SAYAV2.bussines;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;

public class MensajeriaImplTest {
	
	MensajeriaImpl mensajeria;
	Mensaje mensaje;
	

	@Before
	public void setUp() throws Exception {
		mensajeria = MensajeriaImpl.getInstance();
		mensaje = new Mensaje();
		mensaje.setDescripcion("Prueba2");
		mensaje.setEstado(EstadoUtils.PENDIENTE);
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setTipoMensaje(mensajeria.getTipos().getTipo(TipoMensajeUtils.NUEVO_MIEMBRO));
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
		
		
		Date fechaInicial = (Date) mensaje.getFechaCreacion().clone();
		Date fechaReenvio;
		mensajeria.guardarMensaje(mensaje);

		
		
		fechaInicial.setTime(fechaInicial.getTime() + TimeUnit.MINUTES.toMillis(mensaje.getTipoMensaje().getQuantum()));
		
		assertTrue(mensajeria.reenviarMensaje(mensaje, fechaInicial));
		fechaReenvio = mensaje.getFechaReenvio();
		
//		assertNotEquals(fechaInicial.getTime(),fechaReenvio.getTime());
//		
//		mensaje.setEstado(EstadoUtils.CONFIRMADO);
//		
//		assertFalse(mensajeria.reenviarMensaje(mensaje, fechaInicial));
//		
//		fechaInicial.setTime(fechaInicial.getTime() - TimeUnit.MINUTES.toMillis(mensaje.getTipoMensaje().getQuantum()/2));
//		
//		assertFalse(mensajeria.reenviarMensaje(mensaje, fechaInicial));
//		
//		fechaInicial.setTime(mensaje.getFechaCreacion().getTime() + TimeUnit.MINUTES.toMillis(mensaje.getTipoMensaje().getTimetolive()));
//		
//		assertFalse(mensajeria.reenviarMensaje(mensaje, fechaInicial));
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
		msg.setFechaCreacion(new Date());
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
