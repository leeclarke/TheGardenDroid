package util;
import org.apache.log4j.Logger;
import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BaseUnitTest extends UnitTest {
	
	Logger logger = Logger.getLogger(BaseUnitTest.class);
//	@Before
//    public void setup() {
////        Fixtures.deleteAll();
//		//Disabled for now.
//    }


//	@Test
//	public void dummyTest() {
//
//	}
	
	/**
	 * Checks for equality but ignores mils.
	 * @param expected
	 * @param actual
	 */
	public void assertDatesAlmostEqual(Date expected, Date actual){
		if(expected == null  && actual == null) {
			logger.info("Dates are null");
			assertTrue(true);
		}			
		else if(expected == null  || actual == null) {
			assertTrue("One Date is null",false);
		}
		if(expected.equals(actual)){
			logger.info("Dates are null");
			assertTrue(true);
		}
		long dateDif = expected.getTime() - actual.getTime();
		System.out.println("expected="+expected.getTime());
		System.out.println("actual="+actual.getTime());
		System.out.println("#@@@ dateDif==" + dateDif);
		if(dateDif < 1000 && dateDif > -1000 ) {
			assertTrue(true);
		}
		assertTrue("expected: ["+expected + "] actual: [" + actual +"]",false);
	}
}