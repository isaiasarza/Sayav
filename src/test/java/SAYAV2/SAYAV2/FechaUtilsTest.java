package SAYAV2.SAYAV2;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.Utils.FechaUtils;

public class FechaUtilsTest {
	
	Date fechaInicio;
	Date fechaFin;
	static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs


	@Before
	public void setUp() throws Exception {
		fechaInicio = new Date();
		fechaFin = new Date(fechaInicio.getTime() + (10 * ONE_MINUTE_IN_MILLIS));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDiffDays() {
		long diff = FechaUtils.diffDays(fechaInicio, fechaFin);
		assertEquals(diff,10);
	}

}