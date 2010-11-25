package org.dragonfly.gardendroid.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SensorDataFactory {
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-mm-DD HH:MM:SS");

	public static GardenDroidData ParseGardenData(String dataString) throws Exception {
		if(dataString == null || dataString.length() <2 || dataString.indexOf("|")>0) {
			throw new Exception("Invalid data string");
		}
		GardenDroidData data = new GardenDroidData();
		char type = dataString.charAt(0);
		data.setSensorType(SensorType.getByCode(type));
		data.setTimestamp(parseTimeStamp(dataString));
		
		data.dataValues.put("data", parseSingleDataValue(dataString));
		//TODO: Need to set based on type.
		return data;
	}
	
	
	private static Date parseTimeStamp(String data) throws ParseException {
		String dateStr = data.split("|")[0];
		return dateFormat.parse(dateStr);
	}
	
	private static String parseSingleDataValue(String data) {
		String dateStr = data.split("|")[1];
		return dateStr;
	}
	
}
