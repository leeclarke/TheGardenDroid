package functional;
import java.util.regex.Pattern;

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
	static final String sensorPath = "/gardenDroidData/saveTemp";
	static final String currCondPath = "/gardenDroidData/current";
	static final String tempJsonMessage = "{\"tempF\":46.23,\"tempC\":8.5,\"dateTime\":\"Dec 2, 2010 6:16:12 AM\",\"data\":46.23,\"sensorType\":\"TEMPERATURE\"}"; 
	static final String sensorJsonMessage = "{\"dateTime\":\"Dec 2, 2010 7:16:12 AM\",\"data\":126,\"sensorType\":\"MOISTURE\"}";
	
	@Test
    public void testGetCurrentConditions() {
        Response response = GET(currCondPath);
        assertIsOk(response);
        assertContentType("application/json", response);
        assertCharset("utf-8", response);
        System.out.println("CurrCond== "+getContent(response));
        //TODO: Need to figure out how to test this..
        
        
        //assertContentMatch("OK", response);
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
	
	public static void assertContentMatch(String pattern, Response response) {
        Pattern ptn = Pattern.compile(pattern);
        boolean ok = ptn.matcher(getContent(response)).find();
        assertTrue("Response content does not match '" + pattern + "' response = "+getContent(response) , ok);
    }
}
