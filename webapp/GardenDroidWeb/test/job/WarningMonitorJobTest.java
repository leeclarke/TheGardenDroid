package job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jobs.WarningMonitorJob;

import models.SensorData;
import models.SensorType;

import org.junit.Test;

import play.db.jpa.JPABase;

import util.BaseUnitTest;

public class WarningMonitorJobTest extends BaseUnitTest {

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
	public void sendNotification() {
		
	}
}
