package org.dragonfly.gardendroid.dto;

import java.util.Calendar;
import static org.junit.Assert.*;

import org.junit.Test;

public class SensorDataFactoryTest {
	
	@Test(expected=Exception.class)
	public void parseGardenData_InvalidData() throws Exception {
		
		GardenDroidData resp = SensorDataFactory.parseGardenData("$FDSFSDF");
		
	}

	@Test(expected=Exception.class)
	public void parseGardenData_Good_Type_InvalidTimestamp() throws Exception {
		GardenDroidData resp = SensorDataFactory.parseGardenData("TFDSFSDF|");

	}
	
	@Test
	public void parseGardenData_Temp() throws Exception {
		String tempData = "T2010-11-25T22:30:5|25.81|78.4";
		GardenDroidData resp = SensorDataFactory.parseGardenData(tempData);
		assertEquals(SensorType.TEMPERATURE, resp.getSensorType());
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.set(2010, 10, 25, 22, 30, 5);
		expectedDate.set(Calendar.MILLISECOND,0);
		assertEquals(expectedDate.getTime(), resp.getTimestamp());
		
		assertEquals("25.81", (String)resp.getDataValues().get(GardenDroidData.TEMP_C_VALUE));
		assertEquals("78.4", (String)resp.getDataValues().get(GardenDroidData.TEMP_F_VALUE));
	}
	
	@Test
	public void parseGardenData_Mositure() throws Exception {
		String moistData = "M2010-11-25T22:30:5|26";
		GardenDroidData resp = org.dragonfly.gardendroid.dto.SensorDataFactory.parseGardenData(moistData);
		assertEquals(SensorType.MOISTURE, resp.getSensorType());
		
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.set(2010, 10, 25, 22, 30, 5);
		expectedDate.set(Calendar.MILLISECOND,0);
		assertEquals(expectedDate.getTime(), resp.getTimestamp());
		
		assertEquals("26", (String)resp.getSingleDataValue());
	}

	//The RTC device will return no value if the min/sec are == 0
	@Test
	public void parseGardenData_Mositure_OddTimestamp() throws Exception {
		String moistData = "M2010-11-25T22::|26";
		GardenDroidData resp = org.dragonfly.gardendroid.dto.SensorDataFactory.parseGardenData(moistData);
		assertEquals(SensorType.MOISTURE, resp.getSensorType());
		
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.set(2010, 10, 25, 22, 0, 0);
		expectedDate.set(Calendar.MILLISECOND,0);
		assertEquals(expectedDate.getTime(), resp.getTimestamp());
		
		assertEquals("26", (String)resp.getSingleDataValue());
	}
	
	@Test
	public void cleanValidateMessage_good() throws Exception {
		String goodMessage = "T2010-11-25T22:30:5|25.81|78.4";
		String value = SensorDataFactory.cleanValidateMessage(goodMessage);
		assertNotNull(value);
		assertEquals(goodMessage, value);
	}
	
	@Test
	public void cleanValidateMessage_messy() throws Exception {
		String messyMessage = "%j%J%x00T2010-11-25T22:30:5|25.81|78.4";
		String goodMessage = "T2010-11-25T22:30:5|25.81|78.4";
		String value = SensorDataFactory.cleanValidateMessage(messyMessage);
		assertNotNull(value);
		assertEquals(goodMessage, value);
	}
	
	@Test(expected=Exception.class)
	public void cleanValidateMessage_rotten() throws Exception {
		String rottenMessage = "0TJKKJJ%DD0099|00";
		String value = SensorDataFactory.cleanValidateMessage(rottenMessage);
		
	}
	
}
