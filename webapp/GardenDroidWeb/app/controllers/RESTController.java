package controllers;

import java.io.InputStreamReader;
import java.io.Reader;

import models.SensorData;
import models.TempSensorData;
import play.mvc.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RESTController extends Controller {

	/**
	 * REST/JSON services for saving data
	 */
	public static void saveSensorData() {
		try {
			Reader reader = new InputStreamReader(request.body);
			Gson gson = new GsonBuilder().create();
			SensorData sensorData = gson.fromJson(reader, SensorData.class);
			sensorData.save();
			renderJSON("{status:OK}");
		}
		catch (Exception e) {
			renderJSON("{status:Invalid Input}");
		}
	}
	
	/**
	 * REST/JSON services for saving Temp data only
	 */
	public static void saveTempSensorData(){
		try {
			Reader reader = new InputStreamReader(request.body);
			Gson gson = new GsonBuilder().create();
			TempSensorData sensorData = gson.fromJson(reader, TempSensorData.class);
			sensorData.save();
			renderJSON("{status:OK}");
		}
		catch (Exception e) {
			renderJSON("{status:Invalid Input}");
		}
	}
	
	//TODO: Add a GET for CurrentConditions
}
