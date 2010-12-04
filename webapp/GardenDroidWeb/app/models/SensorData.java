package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class SensorData extends Model {

    public Date dateTime;
    public double data;
    public SensorType sensorType;

    public SensorData(Date dateTime, double data, SensorType sensorType) {
        this.dateTime = dateTime;
        this.data = data;
        this.sensorType = sensorType;
    }
    
    /**
     * Helper, Retrieves current data for all Sensors.
     * @return
     */
    public static HashMap<SensorType, SensorData> retrieveLatestSensorData(){
    	HashMap<SensorType, SensorData> sensorMap = new HashMap<SensorType, SensorData>();

		SensorData moist = SensorData.find("sensorType = ? order by dateTime desc", SensorType.MOISTURE).first();
		SensorData ambientLight = SensorData.find("sensorType = ? order by dateTime desc", SensorType.AMBIENT_LIGHT).first();
		SensorData growLight = SensorData.find("sensorType = ? order by dateTime desc", SensorType.GROW_LITE).first();
		SensorData humidity = SensorData.find("sensorType = ? order by dateTime desc", SensorType.HUMIDITY).first();
		SensorData irrigation = SensorData.find("sensorType = ? order by dateTime desc", SensorType.WATER_IRRIGATION).first();
		SensorData tempWarning = SensorData.find("sensorType = ? order by dateTime desc", SensorType.TEMP_WARNING).first();
		
		sensorMap.put(SensorType.MOISTURE, moist);
		sensorMap.put(SensorType.AMBIENT_LIGHT, ambientLight);
		sensorMap.put(SensorType.GROW_LITE, growLight);
		sensorMap.put(SensorType.HUMIDITY, humidity);
		sensorMap.put(SensorType.WATER_IRRIGATION, irrigation);
		sensorMap.put(SensorType.TEMP_WARNING, tempWarning);
    	return sensorMap;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder("[SensorData]");
    	sb.append(" dateTime=").append(dateTime);
    	sb.append(" data=").append(data);
    	sb.append(" sensorType=").append(sensorType);
    	return sb.toString();
    }
}