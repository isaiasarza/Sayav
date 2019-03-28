package SAYAV2.SAYAV2.bussines;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Datos.DatoGrupo;
import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.FechaUtils;
import SAYAV2.SAYAV2.Utils.FileUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.MensajesPendientes;
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
		File file = new File(FileUtils.getUsuarioFile());
		usuarioDao = UsuarioDao.getInstance();
		usuarioDao.setFile(file);
		miembro = new Peer("nuevo_miembro.ddns.net",9898);
		datoGrupo = new DatoGrupo(miembro, usuarioDao.getGrupo(0));
		mensajeria = MensajeriaImpl.getInstance();
		mensajeria.init();
		mensaje = new Mensaje();
		mensaje.setDatos(json.render(datoGrupo));
		mensaje.setDescripcion("Prueba2");
		mensaje.setEstado(EstadoUtils.Estado.PENDIENTE);
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		//mensaje.setTipoMensaje(tiposMensajesDao.getTipo(TipoMensajeUtils.NUEVO_MIEMBRO));
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
			Peer peer = new Peer(usuarioDao.getSubdominio(), 8080);
			try {
				mensaje.setOrigen(peer);
				//mensajeria.propagarMensaje(mensaje, grupo);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGuardarMensajePendiente() {
		fail("Not yet implemented");
	}

	@SuppressWarnings("unused")
	@Test
	public void testReenviarMensajePendiente() {
		
		
		Date fechaInicial = (Date) mensaje.getFechaCreacion().clone();
		Date fechaReenvio;
		mensajeria.guardarMensaje(mensaje);

		
		
		fechaInicial.setTime(fechaInicial.getTime() + TimeUnit.MINUTES.toMillis(mensaje.getTipoMensaje().getQuantum()));
		
		//assertTrue(mensajeria.reenviarMensaje(mensaje, fechaInicial));
		fechaReenvio = mensaje.getFechaReenvio();
		assertTrue(mensajeria.eliminarMensaje(mensaje));
//		assertNotEquals(fechaInicial.getTime(),fechaReenvio.getTime());
//		
//		mensaje.setEstado(EstadoUtils.Estado.CONFIRMADO);
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
		assertTrue(mensajeria.eliminarMensaje(mensaje));
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
			assertTrue(mensajeria.eliminarMensaje(mensaje));
		} catch (Exception e) {
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
	
	@Test
	public void testEstadoZombie(){
		Date fechaFin =  new Date();

		MensajesPendientes mensajes = mensajeria.getMensajes();
		List<Mensaje> aux = new LinkedList<Mensaje>();
		long diff;
		long time;
		for(Mensaje m: mensajes.getMensaje()){
			diff = FechaUtils.diffDays(m.getFechaCreacion(), fechaFin);
			time = TimeUnit.MINUTES.toMillis(m.getTipoMensaje().getTimetolive());
			if(diff >= time && m.getEstado().equals(EstadoUtils.Estado.PENDIENTE)){
				aux.add(m.clone());
			}
		}
		
		assertTrue(aux.isEmpty());
	}
	
	@Test
	public void testEliminarConfirmados(){
		Date fechaFin =  new Date();

		MensajesPendientes mensajes = mensajeria.getMensajes();
		List<Mensaje> aux = new LinkedList<Mensaje>();
		long diff;
		long time;
		for(Mensaje m: mensajes.getMensaje()){
			diff = FechaUtils.diffDays(m.getFechaCreacion(), fechaFin);
			time = TimeUnit.DAYS.toMillis(EstadoUtils.TIME_TO_LIVE_CONF);
			if(diff >= time && m.getEstado().equals(EstadoUtils.Estado.CONFIRMADO)){
				aux.add(m.clone());
			}
		}
		
		assertTrue(aux.isEmpty());
	}

}
