package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import models.SensorData;
import models.SensorType;
import models.TempSensorData;

import org.apache.log4j.Logger;

import play.mvc.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RESTController extends Controller {

	static final Logger logger = Logger.getLogger(RESTController.class);
	/**
	 * REST/JSON services for saving data
	 */
	public static void saveSensorData() {
		String body = "";
		try {
			logger.info("REST POSTED Sensor Data Temp");
			body = streamToString(request.body);
			Gson gson = new GsonBuilder().create();
			SensorData sensorData = gson.fromJson(body, SensorData.class);
			sensorData.save();
			renderJSON("{status:OK}");
		}
		catch (Exception e) {
			logger.warn("Failed to convert REST Post. JSON input: "+body );
			renderJSON("{status:Invalid Input}");
		}
	}
	
	/**
	 * REST/JSON services for saving Temp data only
	 */
	public static void saveTempSensorData(){
		String body = "";
		try {
			logger.info("REST POSTED Sensor Data Temp");
			body = streamToString(request.body);
			
			Gson gson = new GsonBuilder().create();
			TempSensorData sensorData = gson.fromJson(body, TempSensorData.class);
			sensorData.save();
			renderJSON("{status:OK}");
		}
		catch (Exception e) {
			logger.warn("Failed to convert REST Post. JSON input: "+body );
			renderJSON("{status:Invalid Input}");
		}
	}
	
	static String streamToString(InputStream body){
		BufferedReader in = new BufferedReader(new InputStreamReader(body));
		StringBuffer sb = new StringBuffer();
		String line;
		try {
			while ((line = in.readLine()) != null) { // while loop begins here
				sb.append(line);
			}
		} catch (IOException e1) {
			sb.append("Error reading input");
		}
		return sb.toString();
	}
	
	/**
	 * Returns the current conditions in JSON format
	 */
	public static void currentConditions(){
		HashMap<SensorType, SensorData> conds = SensorData.retrieveLatestSensorData();
		conds.put(SensorType.TEMPERATURE, TempSensorData.getCurrentReading());
		//TODO: add in Last Error info as well.
		renderJSON(conds);
	}
	
}
