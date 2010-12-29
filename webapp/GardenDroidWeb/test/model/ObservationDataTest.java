package model;

import java.util.Date;
import java.util.List;

import models.ObservationData;
import models.Plant;
import models.UserDataType;

import org.junit.Test;

import util.BaseUnitTest;

public class ObservationDataTest extends BaseUnitTest {

	@Test
	public void createAndFind() {
		Double dataValue = 3.23;
		Date now = new Date();
		Plant planted = Plant.getActivePlantings().get(0);
		UserDataType uType = (UserDataType) UserDataType.findAll().get(0); 
		ObservationData test = new ObservationData(now,planted, uType, dataValue);
		test.save();
		
		ObservationData resp = ObservationData.findById(test.id);
		assertNotNull(resp);
		assertNotNull(resp.dateCreated);
		assertEquals(planted,resp.plant );
		assertEquals(dataValue,resp.dataValue);
		assertEquals(uType,resp.dataType);
	}
	
	@Test
	public void retrieveObservationsForPlanting() {
		Plant plant = Plant.getActivePlantings().get(0);
		
		List<ObservationData> resp = ObservationData.retrieveObservationsForPlanting(plant);
		assertNotNull(resp);
		assertTrue(resp.size() > 0);
		
	}
}
