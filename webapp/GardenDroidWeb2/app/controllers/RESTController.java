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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.SensorData;
import model.SensorRecordFrequency;
import model.SensorType;
import model.TempSensorData;
import play.Logger;
import play.api.mvc.BodyParser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
	private static org.slf4j.Logger  logger = Logger.underlying();

	
	public static Result showApiDoc(){
		return ok(views.html.restDoc.render());
	}
	
	
	/**
	 * REST/JSON services for saving data
	 */
	public static Result saveSensorData() {
		try {
			logger.debug("REST POSTED Sensor Data Save");
			JsonNode json = request().body().asJson();
			
//			JsonNode json = Json.parse(request().body().asText());
			SensorData sensorIn = null;
			//logger.debug("Jsonin="+json.toString());
			if(json == null){
				try{
				 json = Json.parse(request().body().asText());
				} catch (Exception e){
					logger.warn("Parsing the body text resulted:  ",e);
				}
				 
				 logger.debug("Json was null.");
				 logger.debug("Current STate"+json.toString() );
				logger.debug("Body Text:  "+request().body().asText());
			}
			if(json.findPath("tempF") != null){
				logger.debug("In Temp Data");
				sensorIn = Json.fromJson(json, TempSensorData.class);
			} else{
				logger.debug("In Sensor Data");
				sensorIn = Json.fromJson(json, SensorData.class);
			}
			if(sensorIn != null){
				SensorRecordFrequency srFreq = SensorRecordFrequency.getByType(sensorIn.sensorType);
				if(srFreq == null){
					//populate
					SensorRecordFrequency.getInitSettings();
					srFreq = SensorRecordFrequency.getByType(sensorIn.sensorType);
				}
				//logger.debug("Record Info:  Type:" + srFreq.sensorType + "srFreq.lastPostTime="+srFreq.lastPostTime +"\n\t System.currentTimeMillis()==" + System.currentTimeMillis() + " \n\tsrFreq.lastPostTime+(srFreq.frequencySeconds*1000)=="+(srFreq.lastPostTime+(srFreq.frequencySeconds*1000)) + "\n\tSystem.currentTimeMillis() >  (srFreq.lastPostTime+(srFreq.frequencySeconds*1000)) ==  " +((System.currentTimeMillis() >  (srFreq.lastPostTime+(srFreq.frequencySeconds*1000)))));
				//logger.debug("srFreq.lastPostTime == 0L ==="+(srFreq.lastPostTime == 0L));
				if(srFreq.lastPostTime == null || srFreq.lastPostTime == 0L || System.currentTimeMillis() > (srFreq.lastPostTime+ (srFreq.frequencySeconds*1000)) ) {
					srFreq.lastPostTime = System.currentTimeMillis();
					srFreq.save();
					sensorIn.save();
				}
			}
			return ok();
		} catch (Exception e) { 
			logger.warn("Error parsing json",e);
			logger.warn(LOG_MSG_FAILED_TO_CONVERT_REST_POST_JSON_INPUT +request().body());
			ObjectNode result = Json.newObject();
		    result.put("message", LOG_MSG_FAILED_TO_CONVERT_REST_POST_JSON_INPUT);

			return badRequest(result);
		}
	}

	
//	/**
//	 * REST/JSON services for saving Temp data only
//	 */
//	public static void saveTempSensorData() {
//		String body = "";
//		try {
//			logger.debug("REST POSTED Sensor Data Temp");
//			body = streamToString(request.body);
//			renderJSON(recordDataInput(body,true));
//		} catch (Exception e) {
//			logger.error(LOG_MSG_FAILED_TO_CONVERT_REST_POST_JSON_INPUT + body);
//			logger.error(e.getMessage(),e);
//			renderJSON(STATUS_INVALID_INPUT);
//		}
//	}
////TODO:  This is nearly irrelevant now, only need to identify Temperature so can cast to right object type.
//	/**
//	 * Parses input data and converts to an object, then decides to save data based on SensorRecordFrequency. 
//	 * @param requestBody - json posted to service.
//	 * @return - response result of processing
//	 */
//	static String recordDataInput(String requestBody, boolean isTemp){
//		Gson gson = new GsonBuilder().create();
//		SensorData sensorData;
//		if(isTemp) {
//			if(!requestBody.contains("\"sensorType\":\"TEMPERATURE\""))  //Protect against posting wrong data to temp.
//				return STATUS_INVALID_INPUT;
//			sensorData = gson.fromJson(requestBody, TempSensorData.class);
//		}else {
//			if(requestBody.contains("\"sensorType\":\"TEMPERATURE\""))
//				return STATUS_INVALID_INPUT;
//			sensorData = gson.fromJson(requestBody, SensorData.class);
//		}
//		SensorRecordFrequency srFreq = SensorRecordFrequency.getByType(sensorData.sensorType);
//		logger.debug("Record Info:  Type:" + srFreq.sensorType + "srFreq.lastPostTime="+srFreq.lastPostTime +"\n\t System.currentTimeMillis()==" + System.currentTimeMillis() + " \n\tsrFreq.lastPostTime+(srFreq.frequencySeconds*1000)=="+(srFreq.lastPostTime+(srFreq.frequencySeconds*1000)) + "\n\tSystem.currentTimeMillis() >  (srFreq.lastPostTime+(srFreq.frequencySeconds*1000)) ==  " +((System.currentTimeMillis() >  (srFreq.lastPostTime+(srFreq.frequencySeconds*1000)))));
//		logger.debug("srFreq.lastPostTime == 0L ==="+(srFreq.lastPostTime == 0L));
//		if(srFreq.lastPostTime == null || srFreq.lastPostTime == 0L || System.currentTimeMillis() > (srFreq.lastPostTime+ (srFreq.frequencySeconds*1000)) ) {
//			srFreq.lastPostTime = System.currentTimeMillis();
//			srFreq.save();
//			sensorData.save();
//			return STATUS_OK;
//		}
//		return STATUS_OK_DUPLACATE;
//	}
	
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
	public static Result  currentConditions() {
		HashMap<SensorType, SensorData> conds = SensorData.retrieveLatestSensorData();
		conds.put(SensorType.TEMPERATURE, TempSensorData.getCurrentReading());
		return ok(Json.toJson(conds));
	}

	/**
	 * Builds a listing of the last 24 Temp readings in reverse order.
	 */
	public static Result tempHistory() {
		ArrayList<Double> temps = new ArrayList<Double>();
		List<TempSensorData> tempList = TempSensorData.getTempData(0, 24);
		return ok(Json.toJson(tempList));
	}

}
