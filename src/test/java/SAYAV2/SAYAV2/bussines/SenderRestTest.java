package SAYAV2.SAYAV2.bussines;


import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeNoEnviadoException;
import SAYAV2.SAYAV2.mensajeria.Sender;
import SAYAV2.SAYAV2.mensajeria.SenderRest;
import SAYAV2.SAYAV2.model.Peer;

public class SenderRestTest {
	Sender sender = SenderRest.getInstance();
	Peer origen;
	Peer juan;
	Peer lucas;
	List<Mensaje> mensajes;
	Mensaje mensajeJuan;
	Mensaje mensajeLucas;
	@Before
	public void setUp() throws Exception {
		origen = new Peer("isaunp.ddns.net",20000);
		juan = new Peer("juanarza.ddns.net", 20002);
		lucas = new Peer("lucasboba.ddns.net",20001);
		mensajeJuan = new Mensaje(origen, juan);
		mensajeLucas = new Mensaje(origen,lucas);
		mensajes = new ArrayList<Mensaje>();
	}

	@Test
	public void testSendMensaje() {
		try {
			sender.send(mensajeJuan);
		} catch (MensajeNoEnviadoException e) {
			assumeNoException(e);
		}
	}

	@Test
	public void testSendListOfMensaje() {
		
		for(int i = 0; i < 5; i++) {
			mensajes.add(mensajeJuan.clone());
		}
		for(int i = 0; i < 3; i++) {
			mensajes.add(mensajeLucas.clone());
		}
		assertEquals(sender.send(mensajes).size(),5);
	}

}
