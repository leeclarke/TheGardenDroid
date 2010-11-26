package org.dragonfly.gardendroid.dto;

import java.util.Calendar;

import org.junit.Test;

import junit.framework.TestCase;

public class SensorDataFactoryTest extends TestCase {
	
	@Test(expected=Exception.class)
	public void testParseGardenData_InvalidData() throws Exception {
		
		GardenDroidData resp = SensorDataFactory.ParseGardenData("$FDSFSDF");
		
	}

	@Test(expected=Exception.class)
	public void testParseGardenData_Good_Type_InvalidTimestamp() throws Exception {
		GardenDroidData resp = SensorDataFactory.ParseGardenData("TFDSFSDF|");

	}
	
	public void testParseGardenData_Temp() throws Exception {
		GardenDroidData resp = SensorDataFactory.ParseGardenData("T2010-11-25 22:30:5|23.33|76.56");  //TODO: get temp values that are correct.
		assertEquals(SensorType.TEMPRATURE, resp.getSensorType());
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.set(2010, 11, 25, 22, 30, 5);
		assertEquals(expectedDate.getTime(), resp.getTimestamp());
		
		assertEquals("23.33", resp.getDataValues().get(GardenDroidData.TEMP_C_VALUE));
		assertEquals("76.56", resp.getDataValues().get(GardenDroidData.TEMP_F_VALUE));
	}
	
	public void testParseGardenData_Mositure() throws Exception {
		GardenDroidData resp = SensorDataFactory.ParseGardenData("M2010-11-25 22:30:5|26");
		assertEquals(SensorType.MOISTURE, resp.getSensorType());
		
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.set(2010, 11, 25, 22, 30, 5);
		assertEquals(expectedDate.getTime(), resp.getTimestamp());
		
		assertEquals("", resp.getDataValues().get(GardenDroidData.MOISTURE_VALUE));
	}

}
