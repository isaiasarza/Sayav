package SAYAV2.SAYAV2.bussines;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Datos.DatoVoto;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.dao.VotacionesDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.notificacion.GruposImpl;
import SAYAV2.SAYAV2.notificacion.Votacion;
import SAYAV2.SAYAV2.service.JsonTransformer;

public class GruposImplTest {
	
	GruposImpl grupos;
	Peer miembro;
	Grupo grupo;
	UsuarioDao usuarioDao;
	Usuario usuario;
	File usuarioFile;
	VotacionesDao votacionesDao;
	File votacionesFile;
	JsonTransformer json;
	@Before
	public void setUp() throws Exception {
		usuarioFile = new File("SAYAV");
		usuarioDao = UsuarioDao.getInstance();
		usuario = usuarioDao.cargar(usuarioFile);
		grupos = new GruposImpl();
		miembro = new Peer("lucas.ddns.net");
		grupo = usuario.getGrupos().get(0);
		votacionesFile = new File("votaciones");
		votacionesDao = VotacionesDao.getInstance();
		votacionesDao.setFile(votacionesFile);
		json = new JsonTransformer();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAñadirMiembro() {
		try {
			//grupos.añadirMiembro(grupo, miembro);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testVerMiembros() {
		fail("Not yet implemented");
	}

	@Test
	public void testSolicitarBajaMiembro() {
		fail("Not yet implemented");
	}

	@Test
	public void testAbandonarGrupo() {
		fail("Not yet implemented");
	}

	@Test
	public void testAceptarBajaMiembro() {
		fail("Not yet implemented");
	}

	@Test
	public void testRechazarBajaMiembro() {
		fail("Not yet implemented");
	}

	@Test
	public void testProcesarBajaMiembro() {
		fail("Not yet implemented");
	}

	@Test
	public void testRecibirVoto(){
		try {
			Peer miembro = new Peer("lucasboba.ddns.net");
			Votacion votacion = votacionesDao.cargar(votacionesFile).getVotaciones().get(0);
			DatoVoto datos = new DatoVoto(votacion.getId(), false);
			Mensaje msg = new Mensaje();
			msg.setOrigen("lucasboba.dns.net");
			msg.setDestino("isaias_arza");
			msg.setDatos(json.render(datos));
			this.grupos.recibirVoto(msg);
			
			votacion = votacionesDao.cargar(votacionesFile).getVotaciones().get(0);
			assertTrue(votacion.getVotantes().contains(miembro));
		} catch (JAXBException e) {
			
		} catch (Exception e) {
			
		}
		
		
	}
}
