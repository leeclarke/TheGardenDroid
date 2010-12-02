package org.dragonfly.gardendroid;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.dragonfly.gardendroid.dto.GardenDroidData;
import org.dragonfly.gardendroid.dto.SensorType;
import org.dragonfly.gardendroid.util.RESTClient;

public class RestController {
	//"{\"dateTime\":\"Dec 2, 2010 7:16:12 AM\",\"data\":126,\"sensorType\":\"MOISTURE\"}";
	static final SimpleDateFormat fdate = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a");
	static final Logger logger = Logger.getLogger(RestController.class);
	
	//TODO: Extract to properties file
	public static final String sensorURL = "http://localhost:9000/gardenDroidData/saveData";
	public static final String tempSensorURL = "http://localhost:9000/gardenDroidData/saveTemp";
	
	/**
	 * Posts data item collected to the webapp and database though REST call.
	 * @param gdata
	 * @throws IOException 
	 */
	public static void postDataToServer(GardenDroidData gdata) throws IOException{
		if(gdata != null) {
			URL url = new URL( (gdata.getSensorType() == SensorType.TEMPERATURE)? tempSensorURL: sensorURL);
			
			ByteArrayInputStream stream = new ByteArrayInputStream(gardenDataToJSON(gdata).getBytes("UTF-8"));
			RESTClient.request(logger.isDebugEnabled(), RESTClient.POST, url, "", "",stream);
		}
	}
	
	public static String gardenDataToJSON(GardenDroidData gdata){
		StringBuilder jsonBody = new StringBuilder("{");
		if(gdata != null && gdata.getTimestamp() != null && gdata.getSensorType() != null) {
			
			jsonBody.append("\"dateTime\":\"").append(fdate.format(gdata.getTimestamp())).append("\"");
			
			if(gdata.getSensorType() == SensorType.TEMPERATURE)
			{
				jsonBody.append(",\"tempF\":").append( gdata.getDataValues().get(GardenDroidData.TEMP_F_VALUE) );
				jsonBody.append(",\"data\":").append( gdata.getDataValues().get(GardenDroidData.TEMP_F_VALUE) );
				jsonBody.append(",\"tempC\":").append( gdata.getDataValues().get(GardenDroidData.TEMP_C_VALUE) );
			}
			else {
				jsonBody.append(",\"data\":").append(gdata.getSingleDataValue());
			}
			jsonBody.append(",\"sensorType\":\"").append(gdata.getSensorType().toString()).append("\"");
		}
		jsonBody.append("}");
		return jsonBody.toString();
	}
}
