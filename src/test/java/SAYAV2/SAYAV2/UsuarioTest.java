package SAYAV2.SAYAV2;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.dao.GenericDao;
import SAYAV2.SAYAV2.model.Usuario;

public class UsuarioTest {

	Usuario usuario = new Usuario();
	GenericDao genericDao = new GenericDao();
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
		usuarioFile = new File(usuario.getNombre()+ " " + usuario.getApellido());
		usuarioFile.setWritable(true);
		usuarioFile.setReadable(true);
	}
	
	@Test
	public void guardarUsuarioTest(){
		genericDao.guardar(usuario,usuarioFile);
	}
	
	@Test
	public void cargarUsuarioTest(){
		
		Usuario nuevoUsuario = new Usuario();
		System.out.println(usuarioFile);
		//genericDao.cargar(nuevoUsuario, usuarioFile);
		System.out.println(nuevoUsuario);
	
	}
}
