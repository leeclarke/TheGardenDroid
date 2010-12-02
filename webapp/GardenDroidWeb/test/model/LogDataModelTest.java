package model;
import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class LogDataModelTest extends UnitTest {

    @Test
	public void createAndRetrieveLog() {
	    // Create a new user and save it
	    Date now = new Date();
	    String message = "Test Log message.";
	    new LogData(now, message).save();

	    // Retrieve the user with e-mail address bob@gmail.com
	    LogData log = LogData.find("byMessage", message).first();

	    // Test
	    assertNotNull(log);
	    assertEquals(now, log.dateTime);
	    assertEquals(false, log.isError);
	}

	@Test
		public void createAndRetrieveLog_Error() {
		    // Create a new user and save it
		    Date now = new Date();
		    String message = "Test Eror message.";
		    new LogData(now, message ,true).save();


		    LogData log = LogData.find("byMessage", message).first();

		    // Test
		    assertNotNull(log);
		    assertEquals(now, log.dateTime);
		    assertEquals(true, log.isError);
	}
}
