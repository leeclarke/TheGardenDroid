package model;

import java.util.Calendar;
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
	
	@Test
	public void retrieveObservationsForPlanting_StartEndDate() {
		Plant plant = Plant.getActivePlantings().get(0);
		Date startDate = getDateAdjusted(Calendar.DATE,-90);
		Date endDate = getDateAdjusted(Calendar.DATE,0);
		List<ObservationData> resp = ObservationData.retrieveObservationsForPlanting(plant, startDate, endDate);
		assertNotNull(resp);
		assertTrue(resp.size() > 0);		
	}
	
	
	@Test
	public void retrieveObservationsForPlanting_StartDateOnly() {
		Plant plant = Plant.getActivePlantings().get(0);
		Date startDate = getDateAdjusted(Calendar.DATE,-90);
		List<ObservationData> resp = ObservationData.retrieveObservationsForPlanting(plant, startDate, null);
		assertNotNull(resp);
		assertTrue(resp.size() > 0);		
	}
	
	@Test
	public void retrieveObservationsForPlanting_EndDateOnly() {
		Plant plant = Plant.getActivePlantings().get(0);
		Date endDate = getDateAdjusted(Calendar.DATE,-2);
		List<ObservationData> resp = ObservationData.retrieveObservationsForPlanting(plant, null, endDate);
		assertNotNull(resp);
		assertTrue(resp.size() > 0);		
	}
	
	/**
	 * @param field - Calendar field value
	 * @param adjustBy
	 * @return
	 */
	private Date getDateAdjusted(int field, int adjustBy) {
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.HOUR, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);
		
		startDate.add(field, adjustBy);
		return startDate.getTime();
	}
}
