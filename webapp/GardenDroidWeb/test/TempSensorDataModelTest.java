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

	    TempSensorData sensor = TempSensorData.find("bySensorType", SensorType.TEMPERATURE).first();

	    // Test
	    assertNotNull(sensor);

	    assertEquals(78.23, sensor.data, 0);
	    assertEquals(78.23, sensor.tempF, 0);
	    assertEquals(22.79, sensor.tempC, 0);
	    assertEquals(now, sensor.dateTime);
	}


}
