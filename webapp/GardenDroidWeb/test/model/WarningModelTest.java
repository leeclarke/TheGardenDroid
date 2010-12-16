package model;

import java.util.List;

import models.AlertType;
import models.Warning;

import org.junit.Test;

import play.db.jpa.JPABase;

import util.BaseUnitTest;

public class WarningModelTest  extends BaseUnitTest{

	@Test
	public void createAndRetrieveWarning() {
		Warning test = new Warning("It's getting hot in here", true, AlertType.HIGH_TEMP_THRESHOLD).save();
		
		Warning resp = Warning.findById(test.id);
		assertEquals(test.id, resp.id);
		assertEquals(test.message, resp.message);
		assertEquals(test.alertType, resp.alertType);
		assertNotNull(resp.dateTime);
	}
	
	@Test
	public void getActive() {
		
		List<Warning> resp = Warning.getActive();
		assertTrue(resp.size() > 0);
		//grab first and make sure its isActive
		Warning first = resp.get(0);
		assertTrue("First item returned was NOT active", first.isActive);
	}
	
	@Test
	public void getActiveByType() {
		
		List<Warning> resp = Warning.getActive(AlertType.HIGH_TEMP_THRESHOLD);
		assertTrue(resp.size() > 0);
		//grab first and make sure its isActive
		Warning first = resp.get(0);
		assertTrue("First item returned was NOT active", first.isActive);
		assertEquals(AlertType.HIGH_TEMP_THRESHOLD, first.alertType);
	}
	
	@Test
	public void deactivateType(){
		new Warning("It's getting hot in here", true, AlertType.PLANT_TEMP_LOW).save();
		Warning.deactivateType(AlertType.PLANT_TEMP_LOW);
		
		List<Warning> resp = Warning.getActive(AlertType.PLANT_TEMP_LOW);
		assertTrue("No active Warnings should be returned after deactivating Type",resp.size() == 0);
		
	}
}
