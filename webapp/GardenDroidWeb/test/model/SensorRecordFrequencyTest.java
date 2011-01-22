package model;

import java.util.List;

import models.SensorRecordFrequency;
import models.SensorType;

import org.junit.Test;

import util.BaseUnitTest;
import controllers.OptionsManager;

public class SensorRecordFrequencyTest extends BaseUnitTest {

	@Test
	public void testGetInitSettings() {
		List<SensorRecordFrequency> initList = SensorRecordFrequency.getInitSettings();
		
		assertNotNull(initList);
		assertTrue(initList.size() == SensorType.values().length);
		SensorRecordFrequency recFreq = initList.get(0);
		
		assertEquals(OptionsManager.getDefaultSensorRecordFrequency(), recFreq.frequencySeconds);
	}
	
	@Test
	public void createAndRetrieveSensorRecordFrequency() {
		SensorRecordFrequency.getInitSettings();
		
		SensorRecordFrequency glFreq = SensorRecordFrequency.getByType(SensorType.GROW_LITE);
		glFreq.frequencySeconds = 600;
		glFreq.save();
		
		glFreq = SensorRecordFrequency.getByType(SensorType.GROW_LITE);
		
		assertNotNull(glFreq);
		assertEquals(SensorType.GROW_LITE,glFreq.sensorType);
	    assertEquals(new Integer(600) , glFreq.frequencySeconds );
	}
	
	
	@Test
	public void getAllOrdered() {
		List<SensorRecordFrequency> all = SensorRecordFrequency.getAllOrdered();
		assertNotNull(all);
		assertEquals(SensorType.values().length, all.size());
		assertEquals(SensorType.AMBIENT_LIGHT, all.get(0).sensorType);
	}
}
