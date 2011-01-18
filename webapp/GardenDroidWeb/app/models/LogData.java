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

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class LogData extends Model {

    public Date dateTime;
    public String message;
    public boolean isError;

    public LogData(Date dateTime, String message, boolean isError) {
        this.dateTime = dateTime;
        this.message = message;
        this.isError = isError;
    }

    public LogData(Date dateTime, String message) {
	        this.dateTime = dateTime;
	        this.message = message;
	        this.isError = false;
    }

    public static List getErrors(Integer start, Integer limit) {
    	List<LogData> errList = new ArrayList<LogData>();
    	if(start != null && limit != null){
    		errList = LogData.find("isError = ? order by dateTime desc", true).from(start).fetch(limit);
    	} else 	{
    		errList = LogData.find("isError = ? order by dateTime desc", true).fetch();
    	}
    	return errList;
	}
    
    public static List getErrors() {
    	return getErrors(null,null);
	}
    
    public static List getLogEntries() {
    	return getLogEntries(null,null);
    }
    
    public static List getLogEntries(Integer start, Integer limit) {
    	List<LogData> logList = new ArrayList<LogData>();
    	if(start != null && limit != null){
    		logList = LogData.find("isError = ? order by dateTime desc", false).from(start).fetch(limit);
    	}
    	else {
	    	logList = LogData.find("isError = ? order by dateTime desc", false).fetch();
	    }
    	return logList;
	}
}