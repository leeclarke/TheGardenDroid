package model;
import java.util.Date;

import models.SensorData;
import models.SensorType;
import models.TempSensorData;

import org.junit.Test;

import play.test.UnitTest;

public class SensorDataModelTest extends UnitTest {

    @Test
	public void createAndRetrieveSensorData() {
	    // Create a new user and save it
	    Date now = new Date();

	    new SensorData(now, 22 ,SensorType.MOISTURE).save();
	    SensorData sensor = SensorData.find("byDateTime", now).first();

	    // Test
	    assertNotNull(sensor);

	    assertEquals(22, sensor.data, 0);
	    assertEquals(now, sensor.dateTime);
	}

    @Test
	public void createAndRetrieveSensorDataByType() {
	    // Create a new user and save it

	    SensorData sensor = SensorData.find("sensorType = ? order by dateTime desc", SensorType.MOISTURE).first();
	    
	    // Test
	    assertNotNull(sensor);

	}
}
