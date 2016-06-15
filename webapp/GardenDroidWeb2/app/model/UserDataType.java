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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;

/**
 * User Defined data values relating to a Planting.
 * 
 * @author leeclarke
 */
@Entity
@Table(name = "user_data_type")
public class UserDataType extends Model {
	public static Finder<Long,UserDataType> find = new Finder<Long, UserDataType>(
		    Long.class, UserDataType.class
		  );
	
	public static final UserDataType DEFAULT_PLANT_IRRIGATION = new UserDataType("Plant Irrigation", "Event: Plants were watered by hand or by the Droid.");
	public static final UserDataType DEFAULT_PLANT_YIELD = new UserDataType("Plant Yield", "Weight of harvested crops in local unit of mesurement (lbs).");
	public static final UserDataType DEFAULT_PLANT_HEIGHT = new UserDataType("Plant Height", "Current avg. height at time of entry.");
	
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "user_data_id_seq" )
	public long id;
	
	@Column(name = "description")
	public String description;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "is_active")
	public boolean active;  //when inactive the item wont show up on the input form any longer. Its a reversible form of soft delete. 


	public UserDataType(String name, String description) {
		this.name = name;
		this.description = description;
		this.active = true;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public static List<UserDataType> fetchActiveDataTypes(){
		List<UserDataType> results =  find.where().eq("active", true).orderBy("name").findList();
		return results;
	}
}

