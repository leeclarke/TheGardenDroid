package model;
import org.junit.*;
import java.util.*;

import play.test.*;
import util.BaseUnitTest;
import models.*;

public class LogDataModelTest extends BaseUnitTest {

    @Test
	public void createAndRetrieveLog() {
	    // Create a new user and save it
	    Date now = new Date();
	    String message = "Test Log message.";
	    LogData logD = new LogData(now, message ,false);
	    logD.save();

	    LogData log = LogData.findById(logD.id);

	    // Test
	    assertNotNull(log);
	    assertDatesAlmostEqual(now, log.dateTime);
	    assertEquals(false, log.isError);
	}

	@Test
	public void createAndRetrieveLog_Error() {
		    // Create a new user and save it
		    Date now = new Date();
		    String message = "Test Eror message.";
		    LogData logD = new LogData(now, message ,true);
		    logD.save();


		    LogData log = LogData.findById(logD.id);

		    // Test
		    assertNotNull(log);
		    assertDatesAlmostEqual(now, log.dateTime);
		    assertEquals(true, log.isError);
	}
	
	@Test
	public void createAndRetrieveAll_Errors() {
		List errs = LogData.getErrors();
		assertNotNull(errs);
		assertTrue(errs.size()>0);
		
	}
	
	@Test
	public void createAndRetrieveAll_Logs() {
		List logs = LogData.getLogEntries();
		assertNotNull(logs);
		assertTrue(logs.size()>0);
		
	}
}
