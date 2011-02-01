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
import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * User Defined data values relating to a Planting.
 * 
 * @author leeclarke
 */
@Entity
public class UserDataType extends Model {

	//public enum DataType {STRING, NUMBER};
	
	public static final UserDataType DEFAULT_PLANT_IRRIGATION = new UserDataType("Plant Irrigation", "Event: Plants were watered by hand or by the Droid.");
	public static final UserDataType DEFAULT_PLANT_YIELD = new UserDataType("Plant Yield", "Weight of harvested crops in local unit of mesurement (lbs).");
	public static final UserDataType DEFAULT_PLANT_HEIGHT = new UserDataType("Plant Height", "Current avg. height at time of entry.");
	public String description;
	public String name;
	public boolean active;  //when inactive the item wont show up on the input form any longer. Its a reversible form of soft delete. 
//	public DataType type;

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
		List<UserDataType> results =  UserDataType.find("active = true order by name").fetch();
		return results;
	}
}

