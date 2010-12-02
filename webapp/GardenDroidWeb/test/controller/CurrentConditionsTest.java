package controller;


import java.util.Date;
import java.util.HashMap;

import models.SensorData;
import models.SensorType;

import org.junit.Test;

import controllers.CurrentConditions;

import play.mvc.Http.Response;
import play.test.UnitTest;

public class CurrentConditionsTest extends UnitTest{
	
	@Test
    public void testCurrentConditionsRetrieval() {
		HashMap<SensorType, SensorData> currCondsData = SensorData.retrieveLatestSensorData();

		assertNotNull(currCondsData);
		assertTrue(currCondsData.size()>0);
		assertTrue(currCondsData.containsKey(SensorType.MOISTURE));
		assertTrue(currCondsData.containsKey(SensorType.GROW_LITE));
		assertTrue(currCondsData.containsKey(SensorType.AMBIENT_LIGHT));
		assertTrue(currCondsData.containsKey(SensorType.HUMIDITY));
		
		assertNotNull(currCondsData.get(SensorType.MOISTURE));
		assertNotNull(currCondsData.get(SensorType.GROW_LITE));
		assertNotNull(currCondsData.get(SensorType.AMBIENT_LIGHT));
		assertNotNull(currCondsData.get(SensorType.HUMIDITY));
    }
}
