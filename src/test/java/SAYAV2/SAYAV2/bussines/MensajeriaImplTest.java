package SAYAV2.SAYAV2.bussines;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.mensajeria.TipoMensaje;
import SAYAV2.SAYAV2.model.Peer;

public class MensajeriaImplTest {
	
	MensajeriaImpl mensajeria;

	Peer origen;
	Peer juan;
	Peer lucas;
	List<Mensaje> mensajes;
	Mensaje mensajeJuan;
	Mensaje mensajeLucas;
	@Before
	public void setUp() throws Exception {
		mensajeria = MensajeriaImpl.getInstance();
		origen = new Peer("isaunp.ddns.net",20000);
		juan = new Peer("juanarza.ddns.net", 20002);
		lucas = new Peer("lucasboba.ddns.net",20001);
		mensajeJuan = new Mensaje(origen, juan);
		mensajeJuan.setEstado(EstadoUtils.Estado.PENDIENTE);
		mensajeJuan.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensajeJuan.setTipoMensaje(new TipoMensaje(TipoMensajeUtils.NUEVO_MIEMBRO, 10));
		mensajeLucas = new Mensaje(origen,lucas);
		mensajeLucas.setEstado(EstadoUtils.Estado.PENDIENTE);
		mensajeLucas.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensajeLucas.setTipoMensaje(new TipoMensaje(TipoMensajeUtils.NUEVO_MIEMBRO, 10));

		mensajes = new ArrayList<Mensaje>();
	}

	@After
	public void tearDown() throws Exception {
	}
	

	@Test
	public void testPropagarMensaje() {
		List<Mensaje> procesados;
		mensajes.add(mensajeJuan);
		mensajes.add(mensajeLucas);
		procesados = mensajeria.propagarMensajes(mensajes);
		assertEquals(procesados.get(0).getEstado(),EstadoUtils.Estado.CONFIRMADO);
		assertEquals(procesados.get(1).getEstado(),EstadoUtils.Estado.PENDIENTE);

	}

	

	

}
