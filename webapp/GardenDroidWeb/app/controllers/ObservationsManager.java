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

import org.apache.log4j.Logger;

import models.ObservationData;
import models.Plant;
import models.UserDataType;
import play.Play;
import play.mvc.Controller;
import play.mvc.With;

/**
 * Manages Observation Data for Planted Plants.
 * @author leeclarke
 */
@With(Secure.class)
public class ObservationsManager extends Controller {
	private static final Logger logger = Logger.getLogger(ObservationsManager.class);
		/**
		 * Quick Post simple inserts a new Observation and directs back to PlanyLibrary main page.
		 */
		public static void quickPostObservation(Long plantDataId, Long dataTypeId, Double dataValue, String editpage) {
			logger.warn("Enter quickPostObservation: plantDataId="+plantDataId+", dataTypeId="+dataTypeId + " dataValue="+dataValue);
			
			Plant plant = Plant.findById(plantDataId);
			UserDataType dataType = UserDataType.findById(dataTypeId);
			if(plant != null && dataType != null) {
				new ObservationData(plant, dataType, dataValue).save();
				renderArgs.put("ObservationSucessful", "New observation added!");
			}
			else {
				logger.warn("Failed to save Observation info. plant="+plant + " dataType="+dataType);
			}
			
			if(editpage != null)
				PlantLibraryAdmin.editPlanted(plant.id);
			PlantLibrary.viewPlantData();
		}
		
}
