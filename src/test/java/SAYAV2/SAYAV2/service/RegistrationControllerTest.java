package SAYAV2.SAYAV2.service;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.Test;

public class RegistrationControllerTest {

	@Test
	public void initSectores() throws FileNotFoundException {
		assertEquals(RegistrationController.initSectores().size(),RegistrationController.config());
	}

}
