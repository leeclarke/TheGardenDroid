package controllers;

import play.*;
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
    	
    	//SensorData prevSensorData = SensorData.find("order by dateTime desc").first();
        List<SensorData> recentSensorData = SensorData.find(
            "order by dateTime desc"
        ).from(1).fetch(5);
        
        //TODO: build list of Warning notifications for Temp, water etc...  not built yet.
        //TODO: Get Recent Errors  and Log collections for display. 
        render(lastTempRead, recentSensorData);
    }

}