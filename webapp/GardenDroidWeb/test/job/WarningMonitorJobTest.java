package job;

import java.util.Calendar;
import java.util.Date;

import jobs.WarningMonitorJob;
import models.AlertType;
import models.Options;
import models.SensorData;
import models.SensorType;
import models.TempSensorData;
import models.Warning;

import org.apache.commons.mail.EmailException;
import org.junit.Test;

import play.mvc.Before;
import play.test.Fixtures;
import util.BaseUnitTest;

public class WarningMonitorJobTest extends BaseUnitTest {

	Options options = new Options("lee.k.clarke@gmail.com", true,true,new Double(33),true,new Double(95),true,new Integer(60));
	
	@Before
	public void setUp() {
	    Fixtures.deleteAll();
	    Fixtures.load("initial-data.yml");
	}
	
	@Test
	public void verifyDroidIsOperational(){
		WarningMonitorJob warning =  new WarningMonitorJob();
		
		//get latest sensor data to verify against the results.
		
		boolean resp = warning.verifyDroidIsOperational(10);
		
		SensorData latestData = SensorData.getSensorData(0,1).get(0);
		assertNotNull("No SensorData was returned, test can't continue.",latestData);
		Calendar timeLimit = Calendar.getInstance();
		timeLimit.add(Calendar.MINUTE, -(10));
		
		if(resp == true) {
			assertTrue("Expected Droid to report 'Alive' last Sensor time=" + latestData.dateTime + " time Limit="+timeLimit.getTime() , timeLimit.getTimeInMillis() < latestData.dateTime.getTime());
		} else {
			assertTrue("Expected a Not Alive Indication last Sensor time=" + latestData.dateTime  + " time Limit="+timeLimit.getTime(), timeLimit.getTimeInMillis() > latestData.dateTime.getTime());
		}
	}

	@Test
	public void verifyDroidIsOperational_currentData(){
		//insert new SensorData to ensure we get a true
		SensorData newData = new SensorData(new Date(),1,SensorType.MOISTURE).save();
		WarningMonitorJob warning =  new WarningMonitorJob();
		boolean resp = warning.verifyDroidIsOperational(60);
		assertTrue("SensorData was just created so the results should have been true! ->" + resp,resp);
		
	}
	
	@Test
	public void sendNotification() throws EmailException {
		WarningMonitorJob warning =  new WarningMonitorJob();
		warning.sendNotification(options, "Notification Test", "Just testing the GardenDroid Notification system, this is only a test...");
		
	}

	@Test
	public void isAlertTypeActive() {
		WarningMonitorJob warning =  new WarningMonitorJob();
		boolean  resp = warning.isAlertTypeActive(AlertType.WATER_PUMP_DOWN, options);
		assertTrue("Water pump should return false since it's not yet implemented.", !resp);
		
		Warning testWater = new Warning("Test warning",true,AlertType.WATER_PUMP_DOWN);
		Calendar oldDate = Calendar.getInstance();
		oldDate.add(Calendar.HOUR, -20);
		testWater.dateTime = oldDate.getTime();
		testWater.save();
		resp = warning.isAlertTypeActive(AlertType.WATER_PUMP_DOWN, options);
		assertTrue("Water pump warning was just created and should be active..", resp);
		testWater.delete();//doesn't seem to get deleted by Fixture.. 
	}

	//NOTE: For some reason one of the three following tests will always fail because the TempSensor save doesn't seem to happen fast enough to return in a dependent search. No time to figure this out right now.	
		
	@Test
	public void checkForTempThresholdsAlert_NoAlert() {
		TempSensorData tempresp = new TempSensorData(new Date(),44.2,4).save();
		assertTrue(tempresp.id >1);
			
		WarningMonitorJob warning =  new WarningMonitorJob();
		int  resp = warning.checkForTempThresholdsAlert(options);
		assertEquals(0, resp);
	}

	@Test
	public void checkForTempThresholdsAlert_WithAlertHigh() {
		//Inset high temp and test
		WarningMonitorJob warning =  new WarningMonitorJob();
		new TempSensorData(new Date(),99.2,37.2).save();
		int resp = warning.checkForTempThresholdsAlert(options);
		assertEquals("HIGH Temp Threshold test failed.",1, resp);
	}
	
	@Test
	public void checkForTempThresholdsAlert_WithAlertLow() throws InterruptedException {
		//Set current temp value
		TempSensorData tempresp = new TempSensorData(new Date(),29.2,-2).save();
		assertTrue(tempresp.id >1);
		
		WarningMonitorJob warning =  new WarningMonitorJob();
		int  resp = warning.checkForTempThresholdsAlert(options);
		assertEquals("LOW Temp Threshold test failed.",-1, resp);
	}
}
