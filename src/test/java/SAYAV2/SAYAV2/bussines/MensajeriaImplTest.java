package SAYAV2.SAYAV2.bussines;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Datos.DatoGrupo;
import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.service.JsonTransformer;

public class MensajeriaImplTest {
	
	MensajeriaImpl mensajeria;
	UsuarioDao usuarioDao;
	Mensaje mensaje;
	Grupo grupo;
	Peer miembro;
	DatoGrupo datoGrupo;
	File file;
	Usuario usuario;
	JsonTransformer json;
	@Before
	public void setUp() throws Exception {
		json = new JsonTransformer();
		File file = new File("SAYAV");
		usuarioDao = UsuarioDao.getInstance();
		usuarioDao.setFile(file);
		miembro = new Peer("nuevo_miembro.ddns.net");
		datoGrupo = new DatoGrupo(miembro, usuarioDao.getGrupo(0));
		mensajeria = MensajeriaImpl.getInstance();
		mensajeria.init();
		mensaje = new Mensaje();
		mensaje.setDatos(json.render(datoGrupo));
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
		try {
			Grupo grupo = usuarioDao.getGrupo(0);
			mensaje.setOrigen(usuarioDao.getSubdominio());
			mensajeria.propagarMensaje(mensaje, grupo);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
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
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		try {
			mensajeria.recibirSolicitud(mensaje);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
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
