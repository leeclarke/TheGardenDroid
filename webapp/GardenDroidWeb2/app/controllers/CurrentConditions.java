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
//import java.util.HashMap;
//
//import models.SensorData;
//import models.SensorType;
//import models.TempSensorData;
//import play.mvc.Controller;
//
//public class CurrentConditions extends Controller {
//	
//	/**
//	 * Attempts to retrieve most recent data entry for each SensorType.
//	 */
//	public static void retrieveLatestSensorData() {
//		 
//		TempSensorData temp = TempSensorData.getCurrentReading();
//		HashMap<SensorType, SensorData> currentSensorData = SensorData.retrieveLatestSensorData();
//		render(temp, currentSensorData);
//	}
//}
