package controllers;

import play.*;
import play.db.jpa.JPABase;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	@Before
	static void addDefaults() {
	    renderArgs.put("appTitle", Play.configuration.getProperty("droid.title"));
	    renderArgs.put("appBaseline", Play.configuration.getProperty("droid.baseline"));
	}
	
    public static void index() {
    	TempSensorData lastTempRead = TempSensorData.find("sensorType = ? order by dateTime desc",SensorType.TEMPERATURE).first();
    	
        List<SensorData> recentSensorData = SensorData.find("order by dateTime desc").from(1).fetch(5);
        
        List errors = LogData.getErrors();
        List logs = LogData.getLogEntries();
        
        //TODO: build list of Warning notifications for Temp, water etc...  not built yet.
        render(lastTempRead, recentSensorData, errors, logs);
    }

    /**
     * Gets Sensor data for full view page.
     */
    public static void viewSensors() {
    	//TODO: add pagenation
    	List<SensorData> fullSensorData = SensorData.getSensorData(0, 20);
    	render(fullSensorData);
    }
    
    public static void viewLogs() {
    	//TODO: add pagenation
    	List fullLogs = LogData.getLogEntries();
    	render(fullLogs);
	}
}