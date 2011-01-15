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
package controllers;

import play.*;
import play.db.jpa.JPABase;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	public static final Integer ALL_LOGS = 0;
	public static final Integer LOGS_ONLY = 1;
	public static final Integer ERRORS_ONLY = 2;
	
	
	@Before
	static void addDefaults() {
	    renderArgs.put("appTitle", Play.configuration.getProperty("droid.title"));
	    renderArgs.put("appBaseline", Play.configuration.getProperty("droid.baseline"));
	}
	
    public static void index() {
    	TempSensorData lastTempRead = TempSensorData.find("sensorType = ? order by dateTime desc",SensorType.TEMPERATURE).first();
    	
        List<SensorData> recentSensorData = SensorData.find("order by dateTime desc").from(1).fetch(5);
        
        List errs = LogData.getErrors();
        List logs = LogData.getLogEntries();
        
        List planted = Plant.getActivePlantings();
        
        //TODO: Build another widgit for "Planted Plants" items planted days til harvest, days til water...
        render(lastTempRead, recentSensorData, errs, logs, planted);
    }

    /**
     * Gets Sensor data for full view page.
     */
    public static void viewSensors() {
    	List<SensorData> fullSensorData = SensorData.getSensorData(null, null);
    	render(fullSensorData);
    }
    
    public static void viewLogs() {
    	List fullLogs = LogData.getLogEntries();
    	render(fullLogs);
	}
    
    public static void viewErrors() {
    	List fullLogs = LogData.getErrors();
    	render(fullLogs);
	}
}