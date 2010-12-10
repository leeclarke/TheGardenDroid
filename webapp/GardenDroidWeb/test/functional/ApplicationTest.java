package functional;
import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }
    
    @Test
    public void testviewSensors(){
    	Response response = GET("/sensors");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
        String results = getContent(response);
        assertTrue(results.contains("Sensor Data"));
        assertTrue(results.contains("TEMPERATURE"));
    }
    
    @Test
    public void testviewErrors(){
    	Response response = GET("/errors");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
        String results = getContent(response);
        assertTrue(results.contains("Error Log Data"));
        assertTrue(results.contains("Invalid data message"));
    }
    
    @Test
    public void testviewLogss(){
    	Response response = GET("/logs");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
        String results = getContent(response);
        assertTrue(results.contains("Log Data"));
        assertTrue(results.contains("GardenDroid Startup"));
    }
}