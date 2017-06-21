package SAYAV2.SAYAV2;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import SAYAV2.model.Usuario;
import SAYAV2.service.UsuarioController;

public class UsuarioControllerTest {
	Usuario usuario;

	@Before
	public void setBefore(){
		
		usuario = new Usuario();
		usuario.setNombre("Daniel Isaías");
		usuario.setApellido("Arza");
		usuario.setContraseña("abc");
		usuario.setDireccion("Entre Rios 749");
		usuario.setEmail("isaisaarza@gmail.com");
	}
	
	@Test
	public void registrarUsuarioTest(){
		assertEquals(UsuarioController.registrarUsuario(usuario),"registrationSucceeded");
		assertEquals(UsuarioController.authenticate(usuario.getEmail(), "abc",new HashMap<>()),"authentificationSucceeded");
		
	}
	
	@Test
	public void buscarUsuarioTest(){
//		System.out.println(UsuarioController.authenticate(usuario.getEmail(), usuario.getContraseña(), usuario.getNombre(), usuario.getApellido()));
	}
}
