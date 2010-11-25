package org.dragonfly.gardendroid.dto;

import junit.framework.TestCase;

public class SensorTypeTest extends TestCase {

	
	public void testGetByCode() {
		SensorType typeT = SensorType.getByCode('T');
		assertEquals(SensorType.TEMPRATURE, typeT);
	}

	public void testGetByCodeInvalidType() {
		SensorType typeT = SensorType.getByCode('z');
		assertEquals(SensorType.INVALID, typeT);
	}
		
}
