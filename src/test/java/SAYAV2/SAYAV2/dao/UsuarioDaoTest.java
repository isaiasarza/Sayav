package SAYAV2.SAYAV2.dao;

import static org.junit.Assert.fail;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.model.DispositivoM;
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
		try {
			Usuario usuario = usuarioDao.cargar(file);
			DispositivoM d = new DispositivoM();
			DispositivoM d2 = new DispositivoM();
			d.setNumero("2804600698");
			d.setToken("myToken1");
			usuario.getDispositivosMoviles().add(d);
			d2.setToken("myToken2");
			d2.setNumero("2804585878");
			usuario.getDispositivosMoviles().add(d2);
			usuarioDao.guardar(usuario, file);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

}
