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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import models.PlantData;
import models.ReportType;
import models.ReportUserScript;
import models.SensorRecordFrequency;
import models.UserDataType;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {
	private static final org.apache.log4j.Logger logger = Logger.getLogger(Bootstrap.class);

	public void doJob() {
		// Load Default Observation fields:
		loadObservationFields();
		initSensorRecordTypes();

		// Check if the database is empty
		if (PlantData.count() == 0) {
			Fixtures.load("initial-data.yml");
		}

		loadSampleReports();
	}

	/**
	 * Need to ensure that the SensorRecordFrequency has been inserted in the DB
	 */
	private void initSensorRecordTypes() {
		SensorRecordFrequency.getInitSettings();
	}

	/**
	 * There are a couple of "User Defined Fields" that are not user defined but used by the system for alert checking
	 * and provide default examples of UDFs.
	 */
	private void loadObservationFields() {
		List<UserDataType> types = UserDataType.findAll();
		if (types.size() != 0) {
			UserDataType uType = UserDataType.find("byName", UserDataType.DEFAULT_PLANT_IRRIGATION.name).first();
			if (!uType.active) {
				uType.active = true;
				uType.save();
			}
		} else {
			UserDataType.DEFAULT_PLANT_IRRIGATION.save();
			UserDataType.DEFAULT_PLANT_YIELD.save();
			UserDataType.DEFAULT_PLANT_HEIGHT.save();			
		}

	}

	/**
	 * One time loading of the Sample Report scripts. This method sucks but it's what I have time for right now. Tried
	 * loading yml but the script code caused parsing issues.
	 */
	private void loadSampleReports() {
		if (ReportUserScript.fetchAllScripts().size() <= 0) {
			logger.info("Loading default reports");
			StringBuilder script1 = new StringBuilder();
			script1.append("def now24hrAgo = (new Date())-1;").append("\r\n");
			script1.append("def out = \"\"").append("\r\n");
			script1.append("def pass = 0").append("\r\n");
			script1.append("\r\n");
			script1.append("out += \"{\\\"data\\\":[\"").append("\r\n");
			script1.append("for (s in sensorData) {").append("\r\n");
			script1.append("    if(s.dateTime<now24hrAgo) {").append("\r\n");
			script1.append("        break").append("\r\n");
			script1.append("    }").append("\r\n");
			script1.append("\r\n");
			script1.append("    if(s.sensorType.toString().contains(\"TEMPERATURE\") ) {").append("\r\n");
			script1.append("       if(pass != 0){").append("\r\n");
			script1.append("       	   out += \",\"	").append("\r\n");
			script1.append("       }").append("\r\n");
			script1.append("       out += \"[$s.dateTime.time,$s.data]\"").append("\r\n");
			script1.append("    }").append("\r\n");
			script1.append("    pass++").append("\r\n");
			script1.append("}").append("\r\n");
			script1.append("out += \"],\\\"options\\\":{xaxis: { mode: \\\"time\\\", timeformat: \\\"%m/%d %h:%M\\\"}, grid:{clickable:true}}}\"").append("\r\n");
			script1.append("\r\n");
			script1.append("return out").append("\r\n");
			
			new ReportUserScript("24Hr Temp Chart", "Temp readings over last 24 hours", script1.toString(), null, null, null, false, ReportType.CHART, null).save();

			StringBuilder script2 = new StringBuilder();
			script2.append("def twoDaysAgo = (new Date())-2;").append("\r\n");
			script2.append("def out = \"\"").append("\r\n");
			script2.append("def pass = 0").append("\r\n");
			script2.append("\r\n");
			script2.append("out += \"{\\\"data\\\":[\"").append("\r\n");
			script2.append("for (s in sensorData) {").append("\r\n");
			script2.append("    if(s.dateTime<twoDaysAgo) {").append("\r\n");
			script2.append("        break").append("\r\n");
			script2.append("    }").append("\r\n");
			script2.append("\r\n");
			script2.append("    if(s.sensorType.toString().contains(\"MOISTURE\") ) {").append("\r\n");
			script2.append("       if(pass != 0){").append("\r\n");
			script2.append("       	   out += \",\"	").append("\r\n");
			script2.append("       }").append("\r\n");
			script2.append("       out += \"[$s.dateTime.time,$s.data]\"").append("\r\n");
			script2.append("    }").append("\r\n");
			script2.append("    pass++").append("\r\n");
			script2.append("}").append("\r\n");
			script2.append("out += \"],\\\"options\\\":{xaxis: { mode: \\\"time\\\", timeformat: \\\"%m/%d %h:%M\\\"}, grid:{clickable:true}}}\"").append("\r\n");
			script2.append("\r\n");
			script2.append("return out").append("\r\n");
			
			new ReportUserScript("Moisture levels over past 2 days", "Chart showing the soil moisture levels over 2 day period.", script2.toString(), null, null, null, false, ReportType.CHART, null).save();
			
			
			StringBuilder script3 = new StringBuilder();
			script3.append("/* This demonstrates how to use the plantings and work with each").append("\r\n");
			script3.append(" * plantings observation based data such as yield.*/").append("\r\n");						
			script3.append("def grndTotal = 0.0").append("\r\n");
			script3.append("\r\n");
			script3.append("table.title = \"<b>Total Yield by Plant for 2011</b>\"").append("\r\n");
			script3.append("table.setColumnTitles(\"Total for\",\"Value\")").append("\r\n");
			script3.append("for (p in plantings) {").append("\r\n");
			script3.append(" def total = 0.0").append("\r\n");
			script3.append(" for (o in p.observations) {").append("\r\n");
			script3.append("    if(!o.dataType.name.contains('Yield')) {").append("\r\n");
			script3.append("        continue").append("\r\n");
			script3.append("    }").append("\r\n");
			script3.append("    total += o.dataValue").append("\r\n");
			script3.append(" }").append("\r\n");
			script3.append(" def title = \"&nbsp;&nbsp;&nbsp;\" + p.name").append("\r\n");
			script3.append(" def val = \"<center>\" + total.toString()+ \"</center>\"").append("\r\n");
			script3.append(" table.addRow( title ,val)").append("\r\n");
			script3.append(" grndTotal += total").append("\r\n");
			script3.append("}").append("\r\n");
			script3.append("table.addRow(\"<big>Grand Total</big>\",\"<center><big>\"+grndTotal.toString()+\"</big></center>\")").append("\r\n");
			script3.append("\r\n");
			script3.append("return table.toTable()").append("\r\n");
			
			
			Calendar startDate = Calendar.getInstance();
			startDate.clear();
			startDate.set(2011,0,1);
			
			Calendar endDate = Calendar.getInstance();
			endDate.clear();
			endDate.set(2011,11,31);
			
			new ReportUserScript("Crop Yield Totals - 2011", "Table populated with crop yield Totals for 2011.", script3.toString(), startDate.getTime(), endDate.getTime(), null, false, ReportType.TABLE, null).save();
		}
	}
}