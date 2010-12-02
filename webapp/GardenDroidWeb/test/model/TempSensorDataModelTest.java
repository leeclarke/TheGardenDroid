package model;
import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class TempSensorDataModelTest extends UnitTest {

    @Test
	public void createAndRetrieveSensorData() {
	    // Create a new user and save it
	    Date now = new Date();

	    new TempSensorData(now, 78.23 , 22.79).save();

	    TempSensorData sensor = TempSensorData.find("sensorType = ? order by dateTime desc", SensorType.TEMPERATURE).first();

	    // Test
	    assertNotNull(sensor);

	    assertEquals(78.23, sensor.tempF, .5);
	    assertEquals("value was :"+sensor.data, 78.23, sensor.data, .5);
	    assertEquals(22.79, sensor.tempC, 0);
	    assertEquals(now, sensor.dateTime);
	}

}
