package model;

import models.Options;

import org.junit.Test;

import util.BaseUnitTest;

public class OptionsModelTest  extends BaseUnitTest {

	@Test
	public void createAndRetrieveOptions() {
		String email = "lee.k.clarke@gmail.com";
		Double lowTempThreshold = 32.0;
		Double highTempThreshold = 95.5;
		
		new Options(email, true, true, lowTempThreshold, true, highTempThreshold, false, 60).save();
		
		
		Options resp = Options.find("order by id").first();
		assertNotNull(resp);
		assertEquals(email, resp.email);
		assertTrue(resp.enableWarningNotification);
		assertTrue(!resp.enablePlantedWarnings);
		assertTrue(resp.enableLowTempWarning);
		assertTrue(resp.enableHighTempWarning);
		assertEquals(lowTempThreshold, resp.lowTempThreshold);
		assertEquals(highTempThreshold, resp.highTempThreshold);
		assertEquals(new Integer(60), resp.remoteAliveCheckMins);
	}
}
