/**
 * The GardenDroid, a self monitoring and reporting mini-greenhouse.
 *
 * Copyright (c) 2010-2011 Lee Clarke
 *
 * LICENSE:
 *
 * This file is part of TheGardenDroid (https://github.com/leeclarke/TheGardenDroid).
 *
 * TheGardenDroid is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any
 * later version.
 *
 * TheGardenDroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with TheGardenDroid.  If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import models.SensorData;
import models.SensorRecordFrequency;
import models.SensorType;
import models.TempSensorData;

import org.apache.log4j.Logger;

import play.mvc.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * REST implementation which is used by the external Droid interface to post data. Also used by the web app to update
 * the views with realtime data using ajax.
 * 
 * @author leeclarke
 */
public class RESTController extends Controller {

	private static final String LOG_MSG_FAILED_TO_CONVERT_REST_POST_JSON_INPUT = "Failed to convert REST Post. JSON input: ";
	private static final String STATUS_INVALID_INPUT = "{status:Invalid Input}";
	private static final String STATUS_OK = "{status:OK}";
	private static final String STATUS_OK_DUPLACATE = "{status:OK_Dupe_Per_Freq_Config}";
	static final Logger logger = Logger.getLogger(RESTController.class);

	/**
	 * REST/JSON services for saving data
	 */
	public static void saveSensorData() {
		String body = "";
		try {
			logger.debug("REST POSTED Sensor Data Temp");
			body = streamToString(request.body);
			renderJSON(recordDataInput(body));
		} catch (Exception e) {
			logger.warn(LOG_MSG_FAILED_TO_CONVERT_REST_POST_JSON_INPUT + body);
			renderJSON(STATUS_INVALID_INPUT);
		}
	}

	/**
	 * REST/JSON services for saving Temp data only
	 */
	public static void saveTempSensorData() {
		String body = "";
		try {
			logger.debug("REST POSTED Sensor Data Temp");
			body = streamToString(request.body);
			renderJSON(recordDataInput(body));
		} catch (Exception e) {
			logger.error(LOG_MSG_FAILED_TO_CONVERT_REST_POST_JSON_INPUT + body);
			logger.error(e.getMessage(),e);
			renderJSON(STATUS_INVALID_INPUT);
		}
	}

	/**
	 * Parses input data and converts to an object, then decides to save data based on SensorRecordFrequency. 
	 * @param requestBody - json posted to service.
	 * @return - response result of processing
	 */
	static String recordDataInput(String requestBody){
		Gson gson = new GsonBuilder().create();
		SensorData sensorData = gson.fromJson(requestBody, SensorData.class);
		SensorRecordFrequency srFreq = SensorRecordFrequency.getByType(sensorData.sensorType);
		logger.debug("Record Info:  Type:" + srFreq.sensorType + "srFreq.lastPostTime="+srFreq.lastPostTime +"\n\t System.currentTimeMillis()==" + System.currentTimeMillis() + " \n\tsrFreq.lastPostTime+(srFreq.frequencySeconds*1000)=="+(srFreq.lastPostTime+(srFreq.frequencySeconds*1000)) + "\n\tSystem.currentTimeMillis() >  (srFreq.lastPostTime+(srFreq.frequencySeconds*1000)) ==  " +((System.currentTimeMillis() >  (srFreq.lastPostTime+(srFreq.frequencySeconds*1000)))));
		logger.debug("srFreq.lastPostTime == 0L ==="+(srFreq.lastPostTime == 0L));
		if(srFreq.lastPostTime == null || srFreq.lastPostTime == 0L || System.currentTimeMillis() > (srFreq.lastPostTime+ (srFreq.frequencySeconds*1000)) ) {
			srFreq.lastPostTime = System.currentTimeMillis();
			srFreq.save();
			sensorData.save();
			return STATUS_OK;
		}
		return STATUS_OK_DUPLACATE;
	}
	
	/**
	 * Processes request body as input stream and returns a String object containing posted data.
	 * @param body
	 * @return
	 */
	static String streamToString(InputStream body) {
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
	public static void currentConditions() {
		HashMap<SensorType, SensorData> conds = SensorData.retrieveLatestSensorData();
		conds.put(SensorType.TEMPERATURE, TempSensorData.getCurrentReading());
		renderJSON(conds);
	}

	/**
	 * Builds a listing of the last 24 Temp readings in reverse order.
	 */
	public static void tempHistory() {
		ArrayList<Double> temps = new ArrayList<Double>();
		List<TempSensorData> tempList = TempSensorData.find("order by dateTime desc").from(0).fetch(24);
		for (TempSensorData temp : tempList) {
			temps.add(temp.tempF);
		}
		Collections.reverse(temps);
		renderJSON(temps);
	}

}
