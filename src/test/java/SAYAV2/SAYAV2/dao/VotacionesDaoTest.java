package SAYAV2.SAYAV2.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.Utils.FileUtils;
import SAYAV2.dao.VotacionesDao;
import SAYAV2.notificacion.Votacion;

public class VotacionesDaoTest {
	VotacionesDao votacionesDao;
	File votacionesFile;
	@Before
	public  void setUpBeforeClass() throws Exception {
		votacionesFile = new File(FileUtils.getVotacionesFile());
		votacionesDao = VotacionesDao.getInstance();
		votacionesDao.setFile(votacionesFile);
	}

	@After
	public  void tearDownAfterClass() throws Exception {
	}

	@Test
	public void actualizarTest() {
		try {
			int aFavor = 2;
			boolean finalizo = false;
			Votacion votacion = votacionesDao.cargar(votacionesFile).getVotaciones().get(0);
			aFavor = votacion.getVotantesAFavor();
			votacion.setVotantesAFavor(aFavor + 1);
			finalizo = votacion.isFinalizo();
			votacion.setFinalizo(true);
			votacionesDao.actualizarVotacion(votacion, votacionesFile);
			votacion = votacionesDao.cargar(votacionesFile).getVotaciones().get(0);
			assertTrue(votacion.isFinalizo());
			assertEquals(votacion.getVotantesAFavor(),aFavor + 1);
			
			votacion.setVotantesAFavor(aFavor);
			votacion.setFinalizo(finalizo);
			votacionesDao.actualizarVotacion(votacion, votacionesFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
