/**
 * 
 */
package SAYAV2.SAYAV2.model;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.dao.ConfiguratorDao;

/**
 * @author Naza
 *
 */
public class ConfiguratorTest {
	
	Configurator config;
	ConfiguratorDao configDao;
	File file;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		file = new File("configurator");
		config = new Configurator("tcp://192.168.1.34:1883",29111,4);
		configDao = ConfiguratorDao.getInstance();
		configDao.guardar(config, file);
	}

	/**
	 * Test method for {@link SAYAV2.SAYAV2.model.Configurator#getBroker()}.
	 */
	@Test
	public void testGetBroker() {
		assertEquals(config.getBroker(),"tcp://192.168.1.34:1883");
	}

	/**
	 * Test method for {@link SAYAV2.SAYAV2.model.Configurator#setBroker(java.lang.String)}.
	 */
	@Test
	public void testSetBroker() {
	}

	/**
	 * Test method for {@link SAYAV2.SAYAV2.model.Configurator#getPort()}.
	 */
	@Test
	public void testGetPort() {
	}

	/**
	 * Test method for {@link SAYAV2.SAYAV2.model.Configurator#setPort(java.lang.Integer)}.
	 */
	@Test
	public void testSetPort() {
	}

	/**
	 * Test method for {@link SAYAV2.SAYAV2.model.Configurator#getSectores()}.
	 */
	@Test
	public void testGetSectores() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link SAYAV2.SAYAV2.model.Configurator#setSectores(java.lang.Integer)}.
	 */
	@Test
	public void testSetSectores() {
	}

}
