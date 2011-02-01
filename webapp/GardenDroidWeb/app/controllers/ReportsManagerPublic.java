package controllers;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.ObservationData;
import models.Plant;
import models.ReportType;
import models.ReportUserScript;
import models.SensorData;
import models.SensorType;
import models.TableReport;
import models.TempSensorData;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

public class ReportsManagerPublic   extends Controller{
	static Logger logger = Logger.getLogger(ReportsManagerPublic.class);
	
	@Before
	static void addDefaults() {
	    renderArgs.put("appTitle", Play.configuration.getProperty("droid.title"));
	    renderArgs.put("appBaseline", Play.configuration.getProperty("droid.baseline"));
	}
	
	/**
	 * General landing page for reports.
	 */
	public static void viewReports() {
		String scriptTest = "def now = new Date(); def week = now+foo; return \"Week = \"+week";
		
		List<ReportUserScript> userScripts = ReportUserScript.fetchAllScripts();
		render(scriptTest, userScripts);
	}
	
	
	/**
	 * Generates the user script for display. The default available data objects the script can manipulate are also added.
	 * Included objects are:
	 * 		SensorData - [List]
	 * 		Plantings - [List]
	 * 	AND If Planting is Specified:
	 * 		Planting - object
	 * 		ObservationData - [List]
	 * @param id
	 */
	public static void displayUserReport(Long id) {
		ReportUserScript script = ReportUserScript.findById(id);
		Object scriptResult = "";
		if(script != null) {
			Binding binding = new Binding();
			List<SensorData> sensorData;  // Default to last 90 days just to tighten up volume
			if(script.startDate != null || script.endDate != null){
				logger.warn("SensorData Limit by Date");
				StringBuilder query =  new StringBuilder();
				ArrayList params  = new ArrayList();
				if(script.startDate != null)
				{
					query.append("dateTime > ? ");
					params.add(script.startDate);
					if(script.endDate != null)
						query.append(" AND ");
				}
				if(script.endDate != null)
				{
					query.append("dateTime < ?");
					params.add(script.endDate);
				}
				query.append(" order by dateTime");
				logger.warn(query.toString());
				sensorData = SensorData.find(query.toString(), params.toArray()).fetch();
			} else {
				Calendar startDate = Calendar.getInstance();
				startDate.set(Calendar.HOUR, 0);
				startDate.set(Calendar.MINUTE, 0);
				startDate.set(Calendar.SECOND, 0);
				startDate.set(Calendar.MILLISECOND, 0);
				
				startDate.add(Calendar.DATE, -90);
				sensorData = SensorData.find("dateTime > ? order by dateTime desc", startDate.getTime()).fetch();
				
				for (SensorData sd : sensorData) {
					if(sd.sensorType == SensorType.TEMPERATURE) {
						
						sd.data = ((TempSensorData)sd).tempF;
					}
				}
			}

			if(script.planting != null) {
				Plant Planting = Plant.findById(script.planting.id);
				List<ObservationData> observations = ObservationData.retrieveObservationsForPlanting(Planting);
				
				binding.setVariable("planting", Planting);
				binding.setVariable("ObservationData", observations);
			}
			else {
				List<Plant> plantings ; 
				if(script.activeOnlyPlantings) {
					logger.warn("Active only Plantings");
					plantings =  Plant.getActivePlantings(); 
				}
				else {
					logger.warn("ALL Plantings");
					plantings =  Plant.findAll();
				}
				binding.setVariable("sensorData", sensorData);
				binding.setVariable("plantings", plantings);
				List<ObservationData> observations = ObservationData.findAll();
				binding.setVariable("ObservationData", observations);
			}			
			if(script.reportType == ReportType.TABLE){
				logger.warn("### Report is TableType");
				binding.setVariable("table", new TableReport());
				
			}
			GroovyShell shell = new GroovyShell(binding);
			
			try {
				scriptResult = shell.evaluate(script.script);
				
			} catch(CompilationFailedException e) {
				logger.warn("Report CompilationFailedException");
				scriptResult = e.getMessage();
			} catch (MissingPropertyException m) {
				logger.warn("Report MissingPropertyException");
				scriptResult = m.getMessage();
			} catch (NullPointerException n) {
				logger.warn("Report NullPointerException");
				scriptResult = "Sorry, the script was null or invalid, edit the script and try again.";
			} catch (Exception e) {
				logger.warn("Report Exception");
				scriptResult = "There is an error in your script:"+e.getMessage();
			}
		}
		else {
			script = new ReportUserScript("InvalidScript","","",null,null,null,false, null,null);
			scriptResult = "Sorry, the script was null or invalid, edit the script and try again.";
		}
		logger.warn("ScriptResult == "+scriptResult);
		render(script, scriptResult);
	}
}
