package org.dragonfly.gardendroid.dto;

import junit.framework.TestCase;

public class SensorDataFactoryTest extends TestCase {

	public void testParseGardenData() {
		fail("Not yet implemented");
	}
	
	public void testParseGardenData_InvalidData() {
		SensorDataFactory.ParseGardenData("$FDSFSDF");
	}

	public void testParseGardenData_Good_Type_InvalidTimestamp() {
		SensorDataFactory.ParseGardenData("TFDSFSDF|");

	}

	
	public void testParseGardenData_Temp() {
		fail("Not yet implemented");
	}
	
	public void testParseGardenData_Mositure() {
		fail("Not yet implemented");
	}

}
