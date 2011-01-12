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
import java.util.List;

import play.*;
import play.db.jpa.JPABase;
import play.jobs.*;
import play.test.*;
 
import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
    	//Load Default Observation fields:
        loadObservationFields();
        
        // Check if the database is empty
    	if(PlantData.count() == 0) {
            Fixtures.load("initial-data.yml");
        }
        
        
    }

	/**
	 * There are a couple of "User Defined Fields" that are not user defined but used by the system for alert checking and provide 
	 * default examples of UDFs.
	 */
	private void loadObservationFields() {
		List<UserDataType> types = UserDataType.findAll();
		if(types.size() != 0) {
			UserDataType uType = UserDataType.find("byName", UserDataType.DEFAULT_PLANT_IRRIGATION.name).first();
			if(!uType.active) {
				uType.active = true;
				uType.save();
			}
		}
		else {
			UserDataType.DEFAULT_PLANT_IRRIGATION.save();
		}
			
	}
 
}