package org.dragonfly.gardendroid.dto;

import junit.framework.TestCase;

public class SensorTypeTest extends TestCase {

	
	public void testGetByCode() {
		SensorType typeT = SensorType.getByCode('T');
		assertEquals(SensorType.TEMPERATURE, typeT);
	}

	public void testGetByCodeInvalidType() {
		SensorType typeT = SensorType.getByCode('z');
		assertEquals(SensorType.INVALID, typeT);
	}
		
	public void testGetValidLetters() {
		String codes = SensorType.getValidLetters();
		assertNotNull(codes);
		assertEquals(SensorType.values().length, codes.length());
		System.out.println(codes);
	}
}
