package SAYAV2.SAYAV2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.service.UsuarioController;

public class UsuarioControllerTest {
	Usuario usuario;

	@Before
	public void setBefore(){
		
		usuario = new Usuario();
		usuario.setNombre("Isaías");
		usuario.setApellido("Arza");
		usuario.setContraseña("abc");
		usuario.setDireccion("Entre Rios 749");
		usuario.setEmail("arza.isaias@gmail.com");
	}
	
	@Test
	public void registrarUsuarioTest(){
		assertEquals(UsuarioController.registrarUsuario(usuario),"registrationSucceeded");
	}
}
