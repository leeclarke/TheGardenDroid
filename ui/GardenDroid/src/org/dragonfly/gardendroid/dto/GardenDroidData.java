package org.dragonfly.gardendroid.dto;

import java.util.Date;
import java.util.HashMap;

/**
 * DO for the data messages received from the Arduino GardenDroid. Object creation should be managed by the SensorDataFactory.
 * @author lee clarke
 */
public class GardenDroidData implements SensorData {

	public static final String TEMP_F_VALUE = "TEMPF";
	public static final String TEMP_C_VALUE = "TEMPC";
	public static final String ERROR = "ERROR";
	public static final String SINGLE_DATA_VALUE = "DATA";
	
	private SensorType dataType;
	private Date timestamp;
	protected HashMap<String, Object> dataValues = new HashMap<String, Object>();
	
	public HashMap<String, Object> getDataValues() {
		return dataValues;
	}

	public void setDataValues(HashMap<String, Object> dataValues) {
		this.dataValues = dataValues;
	}

	protected void setSensorType(SensorType dataType) {
		this.dataType = dataType;
	}

	@Override
	public SensorType getSensorType() {
		return dataType;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	/**
	 * Gets the data value assuming its a single data value
	 * @return - single object or null if there is more then 1 data value.
	 */
	public Object getSingleDataValue() {
		return this.dataValues.get(SINGLE_DATA_VALUE);		
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[GardenDroidData] ");
		sb.append("Type: ").append(this.dataType).append(" Timestamp:").append(this.timestamp).append(" Data: ").append(this.dataValues);
		return sb.toString();
	}
}
