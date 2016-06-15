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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

import play.db.ebean.Model;

@Entity
@Table(name="log_data")
public class LogData extends Model {

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "log_data_id_seq" )
	public long id;
	
    @Column(name = "dateTime")
	public DateTime dateTime = new DateTime();
	
    @Column(name = "message")
    public String message;
    
    @Column(name = "is_error")
    public boolean isError;
    
    public static Finder<Long,LogData> find = new Finder<Long, LogData>(
		    Long.class, LogData.class
		  );

    public LogData(DateTime dateTime, String message, boolean isError) {
        this.dateTime = dateTime;
        this.message = message;
        this.isError = isError;
    }

    public LogData(DateTime dateTime, String message) {
	        this.dateTime = dateTime;
	        this.message = message;
	        this.isError = false;
    }

    public static List<LogData> getErrors(Integer start, Integer limit) {
    	int page = Math.floorDiv(start, limit);
    	
    	List<LogData> errList = new ArrayList<LogData>();
    	if(start != null && limit != null){
    		errList = find.where().eq("isError",true).orderBy("dateTime desc").findPagingList(limit)
    			    .setFetchAhead(false)
    			    .getPage(page)
    			    .getList();
    	} else 	{
    		errList = find.where().eq("isError",true).orderBy("dateTime desc").findList();
    	}
    	return errList;
	}
    
    public static List<LogData> getErrors() {
    	return getErrors(null,null);
	}
    
    public static List<LogData> getLogEntries() {
    	return getLogEntries(null,null);
    }
    
    public static List<LogData> getLogEntries(Integer start, Integer limit) {
    	List<LogData> logList = new ArrayList<LogData>();
    	int page = Math.floorDiv(start, limit);
    	if(start != null && limit != null){
    		logList = find.where().eq("isError",false).orderBy("dateTime desc").findPagingList(limit)
    			    .setFetchAhead(false)
    			    .getPage(page)
    			    .getList();
    	}
    	else {
	    	logList = find.where().eq("isError",false).orderBy("dateTime desc").findList();
	    }
    	return logList;
	}
}