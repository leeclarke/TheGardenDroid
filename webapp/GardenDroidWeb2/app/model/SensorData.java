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
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

import play.db.ebean.Model;
import utils.JodaTimeSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity
@Table(name = "sensor_data")
public class SensorData extends Model {
	@Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "sensor_data_id_seq" )
	public long id;
	
	@Column(name = "dateTime")
	@JsonSerialize(using=JodaTimeSerializer.class)
//	@JsonDeserialize(using=JodaDateDeserializer.class)MM-dd-yyyy  Not sure I would ever need this.
	public DateTime dateTime = new DateTime();
	
	@Column(name = "data_value")
    public Double dataValue;
	
	@Column(name = "sensorType")
    public SensorType sensorType;
    
    public static Finder<Long,SensorData> find = new Finder<Long, SensorData>(
		    Long.class, SensorData.class
		  );

    public SensorData(DateTime dateTime, double data, SensorType sensorType) {
        this.dateTime = dateTime;
        this.dataValue = data;
        this.sensorType = sensorType;
    }
    
    /**
     * Helper, Retrieves current data for all Sensors.
     * @return
     */
    public static HashMap<SensorType, SensorData> retrieveLatestSensorData(){
    	HashMap<SensorType, SensorData> sensorMap = new HashMap<SensorType, SensorData>();

		SensorData moist = find.where().eq("sensorType",SensorType.MOISTURE).orderBy("dateTime desc").setMaxRows(1).findUnique();
		SensorData ambientLight = find.where().eq("sensorType", SensorType.AMBIENT_LIGHT).orderBy("dateTime desc").setMaxRows(1).findUnique();
		SensorData growLight = find.where().eq("sensorType", SensorType.GROW_LITE).orderBy("dateTime desc").setMaxRows(1).findUnique();
		SensorData humidity = find.where().eq("sensorType", SensorType.HUMIDITY).orderBy("dateTime desc").setMaxRows(1).findUnique();
		SensorData irrigation = find.where().eq("sensorType", SensorType.WATER_IRRIGATION).orderBy("dateTime desc").setMaxRows(1).findUnique();
		SensorData tempWarning = find.where().eq("sensorType", SensorType.TEMP_WARNING).orderBy("dateTime desc").setMaxRows(1).findUnique();
		
		sensorMap.put(SensorType.MOISTURE, moist);
		sensorMap.put(SensorType.AMBIENT_LIGHT, ambientLight);
		sensorMap.put(SensorType.GROW_LITE, growLight);
		sensorMap.put(SensorType.HUMIDITY, humidity);
		sensorMap.put(SensorType.WATER_IRRIGATION, irrigation);
		sensorMap.put(SensorType.TEMP_WARNING, tempWarning);
    	return sensorMap;
    }
    
    public static List<SensorData> getSensorData(Integer start, Integer limit) {
    	
    	int page = Math.floorDiv(start, limit);
    	
    	List<SensorData> sensorList = new ArrayList<SensorData>();
    	if(start != null && limit != null){
    		sensorList = find.where().orderBy("dateTime desc").findPagingList(limit)
    			    .setFetchAhead(false)
    			    .getPage(page)
    			    .getList();
    	}
    	else {
	    	sensorList = find.where().orderBy("dateTime desc").findList();
	    }
    	return sensorList;
	}
    
//	public static void save(SensorData event) {
//		//check if update, will have an id assigned.
//		
//			SqlUpdate update;
//			if (event.id > 0) {
//				Logger.debug("Update Event:  ");
//				update = Ebean.createSqlUpdate("UPDATE event SET name = :name, event_img = :eventImg, mobile_image = :mobileImage, event_date = :eventDate, draft_start = :draftStart, draft_stage = :draftStage, description = :description, make_public = :makePublic, " +
//						"event_type = :eventType, event_end_date = :eventEndDate, event_bracket = :eventBracket, active_caching = :activeCaching, game_version = :gameVersion, has_league = :hasLeague  WHERE id = :id")
//						.setParameter("name", event.name).setParameter("eventImg", event.eventImg).setParameter("mobileImage", event.mobileImage)
//						.setParameter("eventDate", event.eventDate).setParameter("draftStart", event.draftStart).setParameter("draftStage", event.draftStage).setParameter("eventEndDate", event.eventEndDate)
//						.setParameter("description", event.description).setParameter("makePublic", event.makePublic)
//						.setParameter("eventType", event.eventType).setParameter("eventBracket", event.eventBracket)
//						.setParameter("activeCaching", event.activeCaching).setParameter("gameVersion", event.gameVersion).setParameter("id", event.id)
//						.setParameter("hasLeague", event.hasLeague);
//			} else {
//				Logger.debug("Create Event:  ");
//				update = Ebean.createSqlUpdate("INSERT INTO event (name, event_img, mobile_image, event_date, draft_start, description, make_public, event_type, event_end_date, event_bracket, active_caching, game_version) " +
//						"VALUES (:name, :eventImg, :mobileImage, :eventDate, :draftStart, :description, :makePublic, :eventType, :eventEndDate, :eventBracket, :activeCaching, :gameVersion);")
//						.setParameter("name", event.name).setParameter("eventImg", event.eventImg).setParameter("mobileImage", event.mobileImage)
//						.setParameter("eventDate", event.eventDate).setParameter("draftStart", event.draftStart).setParameter("description", event.description)
//						.setParameter("makePublic", event.makePublic).setParameter("eventType", event.eventType)
//						.setParameter("eventEndDate", event.eventEndDate).setParameter("eventBracket", event.eventBracket)
//						.setParameter("activeCaching", event.activeCaching).setParameter("gameVersion", event.gameVersion);
//			}
//
//			int rows = update.execute();
//			Logger.info("Save Event:  "+ rows);
//		
//	}
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder("[SensorData]");
    	sb.append(" id=").append(id);
    	sb.append(" dateTime=").append(dateTime);
    	sb.append(" dataValue=").append(dataValue);
    	sb.append(" sensorType=").append(sensorType);
    	return sb.toString();
    }
}