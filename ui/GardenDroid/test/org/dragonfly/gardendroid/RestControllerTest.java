package org.dragonfly.gardendroid;
import java.util.Date;

import org.dragonfly.gardendroid.RestController;
import org.dragonfly.gardendroid.dto.GardenDroidData;
import org.dragonfly.gardendroid.dto.SensorDataFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class RestControllerTest {
	String tempData = "T2010-11-25T22:30:5|25.81|78.4";
	String sensorData = "M2010-11-25T22:30:5|126";
	
	static final String tempExpectedJson = "{\"dateTime\":\"Nov 25, 2010 10:30:05 PM\",\"tempF\":78.4,\"data\":78.4,\"tempC\":25.81,\"sensorType\":\"TEMPERATURE\"}"; 
	static final String sensorExpectedJson = "{\"dateTime\":\"Nov 25, 2010 10:30:05 PM\",\"data\":126,\"sensorType\":\"MOISTURE\"}";
	
	
	@Test
	public void testPostingTempToWebApp() throws Exception{
		Date now = new Date();
		GardenDroidData gdata = SensorDataFactory.parseGardenData(tempData);
		gdata.setTimestamp(now); //set to current date so easier to find.
		
		RestController.postDataToServer(gdata);
		//No way to verify at this time but a manual check can be done.
	}
	
	@Test
	public void testGardenDataToJSON() throws Exception {
		Date now = new Date();
		GardenDroidData gdata = SensorDataFactory.parseGardenData(sensorData);
		//gdata.setTimestamp(now); //set to current date so easier to find.
		
		String resp = RestController.gardenDataToJSON(gdata);
		System.out.println(resp);
		assertEquals(sensorExpectedJson,resp);
	}
	
	@Test
	public void testGardenDataToJSON_TEMP() throws Exception {
		Date now = new Date();
		GardenDroidData gdata = SensorDataFactory.parseGardenData(tempData);
		//gdata.setTimestamp(now); //set to current date so easier to find.
		
		String resp = RestController.gardenDataToJSON(gdata);
		System.out.println(resp);
		System.out.println(tempExpectedJson);
		assertEquals(tempExpectedJson,resp);
	}
}
