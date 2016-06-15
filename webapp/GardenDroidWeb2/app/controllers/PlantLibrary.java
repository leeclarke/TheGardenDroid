///**
// * The GardenDroid, a self monitoring and reporting mini-greenhouse.
// *
// * Copyright (c) 2010-2011 Lee Clarke
// *
// * LICENSE:
// *
// * This file is part of TheGardenDroid (https://github.com/leeclarke/TheGardenDroid).
// *
// * TheGardenDroid is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
// * License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any
// * later version.
// *
// * TheGardenDroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
// * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
// * details.
// *
// * You should have received a copy of the GNU General Public License along with TheGardenDroid.  If not, see
// * <http://www.gnu.org/licenses/>.
// *
// */
//package controllers;
//
//import java.util.List;
//
//import models.Plant;
//import models.PlantData;
//import models.UserDataType;
//import play.Play;
//import play.mvc.Before;
//import play.mvc.Controller;
//import play.mvc.With;
//
///**
// * Un-secured portion of the PlantLibrary which enables browsing of the data by anyone.
// * @author leeclarke
// */
//public class PlantLibrary extends Controller {
//
//	@Before
//	static void addDefaults() {
//	    renderArgs.put("appTitle", Play.configuration.getProperty("droid.title"));
//	    renderArgs.put("appBaseline", Play.configuration.getProperty("droid.baseline"));
//	}
//	
//	/**
//	 * Load up plant Library for browse and search
//	 */
//	public static void viewPlantData() {
//		
//		List<PlantData> plants = PlantData.find("order By name").fetch();
//		
//		List<Plant> plantings = Plant.find("isActive = ?", true).fetch();
//		List<UserDataType> activeUserTypes = UserDataType.fetchActiveDataTypes();
//		render(plants,plantings,activeUserTypes);
//	}
//	
//	/**
//	 * Returns all Plantings for browsing.
//	 */
//	public static void viewAllPlantings() {
//		List<Plant> plants = Plant.find("order by datePlanted desc").fetch();
//		render(plants);
//	}
//}
