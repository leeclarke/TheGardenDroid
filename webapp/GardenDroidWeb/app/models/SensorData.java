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
package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

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
    
    public static List<SensorData> getSensorData(Integer start, Integer limit) {
    	List<SensorData> sensorList = new ArrayList<SensorData>();
    	if(start != null && limit != null){
    		sensorList = SensorData.find("order by dateTime desc").from(start).fetch(limit);
    	}
    	else {
	    	sensorList = SensorData.find("order by dateTime desc").fetch();
	    }
    	return sensorList;
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