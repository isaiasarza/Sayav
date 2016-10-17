package SAYAV2.SAYAV2;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Usuario;

public class UsuarioTest {

	Usuario usuario = new Usuario();
	UsuarioDao usuarioDao = UsuarioDao.getInstance();
//	String path = "C:\Documents and Settings\Usuario\workspace_desarrollo\SAYAV2";
	File usuarioFile;
	@Before
	public void setBefore(){
		usuario.setApellido("Arza");
		usuario.setNombre("Juan");
		usuario.setContraseña("12345");
		usuario.setDireccion("Entre Rios 749");
		usuario.setEmail("arza.juan@gmail.com");
		usuario.setSubdominio("juanjo");
		usuario.setTelefono("2804998877");
		usuarioFile = new File("SAYAV");
		usuarioFile.setWritable(true);
		usuarioFile.setReadable(true);
	}
	
	@Test
	public void guardarUsuarioTest(){
		usuarioDao.guardar(usuario,usuarioFile);
		usuario.setAlarmaHabilitada(true);
	}
	
	@Test
	public void cargarUsuarioTest() throws JAXBException{
		
		Usuario nuevoUsuario = new Usuario();
		System.out.println(usuarioFile);
		usuarioDao.cargar(usuarioFile);
		System.out.println(nuevoUsuario);
	
	}
}
