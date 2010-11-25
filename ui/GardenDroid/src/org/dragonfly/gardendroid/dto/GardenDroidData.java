package org.dragonfly.gardendroid.dto;

import java.util.Date;
import java.util.HashMap;

public class GardenDroidData implements SensorData {

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
}
