package SAYAV2.SAYAV2.bussines;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;

public class ControllerMQTTTest {
	
	ControllerMQTT controller;
	UsuarioDao usuarioDao;
	Usuario usuario;
	Peer p;
	Grupo g;

	@Before
	public void setUp() throws Exception {
		usuario = new Usuario();
		usuario.setNombre("Alfredo");
		controller = ControllerMQTT.getInstance();
		p = new Peer();
		p.setDireccion("jorgetoconas.ddns.net");
		g = new Grupo();
		g.setNombre("Vecinos");
	}

	@Test
	public void testNotificarNuevoGrupo() {
//		String msg = controller.notificarNuevoGrupo(p, g);
//		System.out.println(msg);
	}

	@Test
	public void testNotificarMiembros() {
//		String msg = controller.notificarMiembros(p, g);
//		System.out.println(msg);
	}
	
	@Test
	public void testArriboNuevoGrupo(){
//		g.setNombre("Familia");
//		p.setDireccion("facundopaez.ddns.net");
////		String msg = controller.notificarNuevoGrupo(p, g);
//		assertTrue(controller.arriboNuevoMiembro(msg, usuario));
//		assertFalse(controller.arriboNuevoMiembro(msg, usuario));
//		System.out.println(usuario);
	}
	
	@Test
	public void testArriboNuevoMiembro(){
//		usuario.addGrupo("Trabajo");
//		g.setNombre("Trabajo");
//		p.setDireccion("lucasbonnanno.ddns.net");
//		String msg = controller.notificarNuevoGrupo(p, g);
///*		assertTrue(controller.arriboNuevoGrupo(msg, usuario));
//*/		System.out.println(usuario);
	}
	


}
