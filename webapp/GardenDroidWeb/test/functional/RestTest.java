package functional;
import java.util.regex.Pattern;

import net.sf.oval.constraint.AssertTrue;

import org.junit.Test;

import com.sun.jndi.url.corbaname.corbanameURLContextFactory;

import play.mvc.Http.Response;
import play.test.FunctionalTest;


/**
 * Test Rest functions to ensure they work right!
 * @author leeclarke
 */
public class RestTest extends FunctionalTest {
	static final String jsonContentType = "application/json";
	static final String tempPath = "/gardenDroidData/saveTemp";
	static final String sensorPath = "/gardenDroidData/saveData";
	static final String currCondPath = "/gardenDroidData/current";
	static final String tempHistPath = "/gardenDroidData/tempHistory";
	static final String tempJsonMessage = "{\"tempF\":46.23,\"tempC\":8.5,\"dateTime\":\"Dec 2, 2010 6:16:12 AM\",\"data\":46.23,\"sensorType\":\"TEMPERATURE\"}"; 
	static final String sensorJsonMessage = "{\"dateTime\":\"Dec 2, 2010 7:16:12 AM\",\"data\":126,\"sensorType\":\"MOISTURE\"}";
	
	@Test
	public void testTempHistoryRest(){
		Response response = GET(tempHistPath);
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset("utf-8", response);
		String jsonResp = getContent(response);
		assertTrue(jsonResp.length() >1);
		assertTrue(jsonResp.startsWith("["));
		assertTrue(jsonResp.lastIndexOf("]") == (jsonResp.length()-1));
	}
	
	@Test
    public void testGetCurrentConditions() {
        Response response = GET(currCondPath);
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
        String jsonResp = getContent(response);
        assertTrue(jsonResp.startsWith("{"));
        assertTrue(jsonResp.contains("TEMPERATURE"));  //should be at least 1

    }
	
	@Test
    public void testPostingOfTempSensorData_Invalid() {
        Response response = POST(newRequest(), tempPath , jsonContentType , "@#$%^JUST JUNK%^$%f");
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
        assertEquals("", "{status:Invalid Input}", getContent(response));
    }
	
	@Test
    public void testPostingOfTempSensorData() {
        Response response = POST(newRequest(), tempPath , jsonContentType , tempJsonMessage);
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
        assertContentMatch("OK", response);
    }
	
	@Test
    public void testPostingOfSensorData() {
        Response response = POST(newRequest(), sensorPath , jsonContentType , sensorJsonMessage);
  
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
        
        assertContentMatch("OK", response);
    }
	
	@Test
    public void testPostingOfSensorData_Invalid() {
        Response response = POST(newRequest(), sensorPath , jsonContentType , "%$^%#Tsdfafsdf_JUNK%^%^%T");
  
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
        assertEquals("", "{status:Invalid Input}", getContent(response));
    }
	
	public static void assertContentMatch(String pattern, Response response) {
        Pattern ptn = Pattern.compile(pattern);
        boolean ok = ptn.matcher(getContent(response)).find();
        assertTrue("Response content does not match '" + pattern + "' response = "+getContent(response) , ok);
    }
	
	
}
