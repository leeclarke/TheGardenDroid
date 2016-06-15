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
package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.joda.time.DateTime;

@Entity
@Table(name="sensor_data")
public class TempSensorData extends SensorData {

	@Column(name = "tempF")
    public double tempF;
	
	@Column(name = "tempC")
    public double tempC;
	
	public static Finder<Long,TempSensorData> find = new Finder<Long, TempSensorData>(
		    Long.class, TempSensorData.class
		  );

    public TempSensorData(DateTime dateTime, double tempF, double tempC) {
        super(dateTime,tempF,SensorType.TEMPERATURE);
        this.tempF = tempF;
        this.tempC = tempC;
    }
    

	/**
	 * Helper method because it gets called more then once.
	 * @return
	 */
	public static TempSensorData getCurrentReading() {
		TempSensorData temp = (TempSensorData) find.where().eq("sensorType", SensorType.TEMPERATURE).orderBy("dateTime desc").setMaxRows(1).findUnique();
		return temp;
	}
	
    public static List<TempSensorData> getTempData(Integer start, Integer limit) {
    	
    	int page = Math.floorDiv(start, limit);
    	
    	List<TempSensorData> sensorList = new ArrayList<>();
    	if(start != null && limit != null){
    		sensorList = find.where().eq("sensorType", ""+SensorType.TEMPERATURE.getCode()).orderBy("dateTime desc").findPagingList(limit)
    			    .setFetchAhead(false)
    			    .getPage(page)
    			    .getList();
    	}
    	else {
	    	sensorList = find.where().eq("sensorType", ""+SensorType.TEMPERATURE.getCode()).orderBy("dateTime desc").findList();
	    }
    	return sensorList;
	}
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(super.toString());
    	sb.append(" tempF=").append(tempF);
    	sb.append(" tempC=").append(tempC);
    	return sb.toString();
    }
}