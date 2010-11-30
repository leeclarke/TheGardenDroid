import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class SensorDataModelTest extends BaseUnitTest {

    @Test
	public void createAndRetrieveSensorData() {
	    // Create a new user and save it
	    Date now = new Date();

	    String notes = "Test Log message.";
	    new SensorData(now, 22 ,SensorType.MOISTURE).save();

	    SensorData sensor = SensorData.find("bySensorType", SensorType.MOISTURE).first();

	    // Test
	    assertNotNull(sensor);

	    assertEquals(22, sensor.data, 0);
	    assertEquals(now, sensor.dateTime);
	}


}
