package SAYAV2.SAYAV2.dao;

import static org.junit.Assert.fail;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Usuario;

public class UsuarioDaoTest {
	private File file;
	private UsuarioDao usuarioDao;

	@Before
	public void setUp() throws Exception {
		file = new File("SAYAV");
		usuarioDao = UsuarioDao.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGuardar() {
		fail("Not yet implemented");
	}

	@Test
	public void testCargar() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testCargarMiembro() {
		Grupo g;
		try {
			Usuario usuario = usuarioDao.cargar(file);
			g = usuario.getGrupos().get(0);
			System.out.println(g);
			g.addPeer("lucas.ddns.net");
			usuarioDao.guardar(usuario, file);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

}
