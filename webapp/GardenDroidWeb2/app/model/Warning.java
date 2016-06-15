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

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

import play.db.ebean.Model;

/**
 * Logs a warning event which may coincide with an email notification if the option is enabled.
 * @author leeclarke
 */
@Entity
@Table(name = "warning")
public class Warning extends Model {
	public static Finder<Long,Warning> find = new Finder<Long, Warning>(
		    Long.class, Warning.class
		  );
	
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "user_data_id_seq" )
	public long id;
	
	@Column(name = "date_time")
	public DateTime dateTime;
	
	@Column(name = "message")
	public String message;
	
	@Column(name = "is_active")
    public boolean isActive;
	
	@Column(name = "alert_type")
    public AlertType alertType;
    
	
    public Warning(String message, boolean isActive, AlertType alertType){
    	this.dateTime = DateTime.now();
    	this.message = message;
    	this.isActive = isActive;
    	this.alertType = alertType;
    }

    /**
     * Marks any Active Warnings of the given type to false
     * @param aType
     */
    public static void deactivateType(AlertType aType) {
		if(aType != null){
			List<Warning> activeList = getActive(aType);
			for (Warning warning : activeList) {
				warning.isActive = false;
				warning.save();
			}
		}		
	}
    
	/**
	 * Gets all active Warnings.
	 * @return 
	 */
	public static List<Warning> getActive() {
		return getActive(null);		
	}
	
	public static List<Warning> getActive(AlertType aType) {
		List<Warning> active;
		if(aType != null) {
			active = find.where().eq("alert_type", aType).eq("is_active", true).orderBy("date_time desc").findList();
		} else {
			active = find.where().eq("is_active", true).orderBy("date_time desc").findList();
		}		
		return active;
	}
	
}
