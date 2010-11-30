package org.dragonfly.gardendroid.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Takes a data input string from GardenDroid and converts it to an object for later persistence.
 * @author lee clarke
 */
public class SensorDataFactory {
	
	static Pattern msgPattern = Pattern.compile("["+SensorType.getValidLetters()+"]\\d{4}-\\d{2}-\\d{2}[T]\\d{1,2}:\\d{1,2}:\\d{1,2}\\|");
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * Takes a message from the Droid sent through RF and attempts to clean it up and parse it into a valid GArdenDroidData object.
	 * @param dataString - message to attempt to parse
	 * @return a good object.
	 * @throws Exception
	 */
	public static GardenDroidData parseGardenData(String dataString)
			throws Exception {
		dataString = cleanValidateMessage(dataString);
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
			case GROW_LITE:
			case WATER_IRRIGATION:
			case TEMP_WARNING:
			case LOG:
				if (tokens.length > 1) {
					data.dataValues.put(GardenDroidData.SINGLE_DATA_VALUE, tokens[1]);
				} else {
					data.dataValues.put(GardenDroidData.ERROR,"Failed to recieve Data Value. Type= "+data.getSensorType()+" Data Recieved:"+ dataString);
				}
				break;
				
			case TEMPERATURE:
				if (tokens.length >= 3) {
					data.dataValues.put(GardenDroidData.TEMP_C_VALUE, tokens[1]);
					data.dataValues.put(GardenDroidData.TEMP_F_VALUE, tokens[2]);
				} else {
					data.dataValues.put(GardenDroidData.ERROR,"Failed to recieve Data Value. Type= "+data.getSensorType()+" Data Recieved:"+ dataString);
				}
				break;
	
			default:
				data.dataValues.put(GardenDroidData.ERROR,("Failed to recieve Data Value. Type= "+data.getSensorType()+" Data Recieved:"+ dataString));
				break;
			}

		return data;
	}
	
	/**
	 * Validates the message and fixes and cleans up the message if needed.
	 * @param message
	 * @return - clean message
	 * @throws Exception - if invalid.
	 */
	public static String cleanValidateMessage(String message) throws Exception{
		String result = "";
		if(message != null)
		{
			Matcher matcher = msgPattern.matcher(message);
			if(matcher.find())
			{
				//Clear off any junk at start of message if junk was prepended to message due to signal quality. 
				result = message.substring(matcher.start());
			}
			else {
				throw new Exception("Invalid message content. msg=" + message);
			}
		}
		return result;
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
