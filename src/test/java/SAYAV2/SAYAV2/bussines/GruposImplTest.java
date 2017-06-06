package SAYAV2.SAYAV2.bussines;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.notificacion.GruposImpl;

public class GruposImplTest {
	
	GruposImpl grupos;
	Peer miembro;
	Grupo grupo;
	UsuarioDao usuarioDao;
	Usuario usuario;
	File usuarioFile;
	@Before
	public void setUp() throws Exception {
		usuarioFile = new File("SAYAV");
		usuarioDao = UsuarioDao.getInstance();
		usuario = usuarioDao.cargar(usuarioFile);
		grupos = new GruposImpl();
		miembro = new Peer("lucas.ddns.net");
		grupo = usuario.getGrupos().get(0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAñadirMiembro() {
		try {
			grupos.añadirMiembro(grupo, miembro);
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

}
