package controllers;

import java.util.HashMap;

import models.SensorData;
import models.SensorType;
import models.TempSensorData;
import play.mvc.Controller;

public class CurrentConditions extends Controller {
	
	/**
	 * Attempts to retrieve most recent data entry for each SensorType.
	 */
	public static void retrieveLatestSensorData() {
		 
		TempSensorData temp = TempSensorData.getCurrentReading();
		HashMap<SensorType, SensorData> currentSensorData = SensorData.retrieveLatestSensorData();
		render(temp, currentSensorData);
	}
}
