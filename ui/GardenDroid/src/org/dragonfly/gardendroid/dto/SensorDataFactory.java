package org.dragonfly.gardendroid.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Takes a data input string from GardenDroid and converts it to an object for later persistence.
 * @author lee clarke
 */
public class SensorDataFactory {

	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static GardenDroidData parseGardenData(String dataString)
			throws Exception {
		if (dataString == null || dataString.length() < 2
				|| dataString.indexOf("|") < 0) {
			throw new Exception("Invalid data string");
		}
		GardenDroidData data = new GardenDroidData();

		char type = dataString.charAt(0);
		String[] tokens = dataString.split("\\|");

		data.setSensorType(SensorType.getByCode(type));
		if (tokens.length > 0) {
			data.setTimestamp(parseTimeStamp(tokens[0]));
		}

		//Process the data value[s] in the message
		switch (data.getSensorType()) {
			case AMBIENT_LIGHT:
			case MOISTURE:
			case HUMIDITY:
			case ERROR:
			case LOG:
				if (tokens.length > 1) {
					data.dataValues.put(GardenDroidData.SINGLE_DATA_VALUE, tokens[1]);
				} else {
					data.dataValues.put(GardenDroidData.ERROR,"Failed to recieve Data Value.");
				}
				break;
				
			case TEMPERATURE:
	
				if (tokens.length >= 3) {
					data.dataValues.put(GardenDroidData.TEMP_C_VALUE, tokens[1]);
					data.dataValues.put(GardenDroidData.TEMP_F_VALUE, tokens[2]);
				} else {
					data.dataValues.put(GardenDroidData.ERROR,"Failed to recieve Data Value.");
				}
				break;
	
			default:
				data.dataValues.put(GardenDroidData.ERROR,("Failed to recieve Data Value. Data Recieved:"+ dataString));
				break;
			}

		return data;
	}

	/**
	 * Converts timestamp to Date correcting any flaws in the data format that
	 * are sent from the RTC output. It will leave a 0 value blank which happens
	 * for min and sec.
	 * 
	 * @param dateString
	 * @return
	 * @throws ParseException
	 */
	private static Date parseTimeStamp(String dateString) throws ParseException {
		String dateStr = dateString.substring(1);
		dateStr = dateStr.replace('T', ' ');
		if (dateStr.length() < 19) { // it may be missing a 0 digit in mins/secs
			if (dateStr.lastIndexOf(':') == dateStr.length() - 1) {
				dateStr += "0";
			}
			if (dateStr.contains("::")) {
				dateStr = dateStr.replace("::", ":0:");
			}
		}

		return dateFormat.parse(dateStr);
	}
}
