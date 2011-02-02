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
		List<ReportUserScript> userScripts = ReportUserScript.fetchAllScripts();
		render( userScripts);
	}
	
	
	/**
	 * Generates the user script for display. The default available data objects the script can manipulate are also added.
	 * Included objects are:
	 * 		SensorData - [List]
	 * 		Plantings - [List]
	 * 	AND If Planting is Specified:
	 * 		Planting - object
	 * @param id
	 */
	public static void displayUserReport(Long id) {//Type, date range, plant, ActiveOnly, retrieve results for Sensor,Plant, observations
		ReportUserScript script = ReportUserScript.findById(id);
		Object scriptResult = "";
		if(script != null) {
			Binding binding = new Binding();
			
			List<SensorData> sensorData = getSensorData(script);  
			fixTemp(sensorData);

			List<Plant> plantings = getPlantings(script); 
			
			loadObservations(plantings, script);
			
			binding.setVariable("sensorData", sensorData);
			if(plantings.size() == 1) {
				binding.setVariable("planting", plantings.get(0));
			}
			else{
				binding.setVariable("plantings", plantings);
			}
			
			if(script.reportType == ReportType.TABLE){
				binding.setVariable("table", new TableReport());
			}
			scriptResult = processScript(script.script, binding);
		}
		else {
			script = new ReportUserScript("InvalidScript","","",null,null,null,false, null,null);
			scriptResult = "Sorry, the script was null or invalid, edit the script and try again.";
		}
		logger.debug("ScriptResult == "+scriptResult);
		render(script, scriptResult);
	}
	
	/**
	 * Inserts the Observations in the Plant objects, this should probably be done with Hibernate but this is simple and quick.
	 * @param plantings
	 * @param script
	 */
	private static void loadObservations(List<Plant> plantings, ReportUserScript script) {
		plantings.size();
		for (Plant planting : plantings) {
			if(script.startDate != null || script.endDate != null){
				planting.observations = ObservationData.retrieveObservationsForPlanting(planting, script.startDate, script.endDate);
			} else {
				planting.observations = ObservationData.retrieveObservationsForPlanting(planting);
			}
		}
	}

	/**
	 * @param script
	 * @return
	 */
	private static List<Plant> getPlantings(ReportUserScript script) {
		List<Plant> plantings;
		if(script.planting != null) {
			plantings = new ArrayList<Plant>();
			plantings.add((Plant) Plant.findById(script.planting.id));
		}
		else {
			if(script.activeOnlyPlantings) {
				plantings =  Plant.getActivePlantings(); 
			}
			else {
				logger.warn("ALL Plantings");
				plantings =  Plant.findAll();
			}
			
		}	
		return plantings;
	}

	/**
	 * Ensures that the Temp data is set up correctly.
	 * @param sensorData
	 */
	private static void fixTemp(List<SensorData> sensorData) {
		for (SensorData sd : sensorData) {
			if(sd.sensorType == SensorType.TEMPERATURE) {
				
				sd.data = ((TempSensorData)sd).tempF;
			}
		}
	}

	/**
	 * Default to last 90 days just to tighten up volume if no date range is specified.
	 * @param script
	 * @return
	 */
	private static List<SensorData> getSensorData(ReportUserScript script) {
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
			return SensorData.find(query.toString(), params.toArray()).fetch();
		} else {
			Calendar startDate = Calendar.getInstance();
			startDate.set(Calendar.HOUR, 0);
			startDate.set(Calendar.MINUTE, 0);
			startDate.set(Calendar.SECOND, 0);
			startDate.set(Calendar.MILLISECOND, 0);
			
			startDate.add(Calendar.DATE, -90);
			return  SensorData.find("dateTime > ? order by dateTime desc", startDate.getTime()).fetch();
		}
	}

	/**
	 * Just processes the script and handles any resulting errors.
	 * @param script
	 * @param binding
	 * @return  - currently expects just a string.
	 */
	private static Object processScript(String script, Binding binding){
		Object scriptResult = "";
		GroovyShell shell = new GroovyShell(binding);
		
		try {
			scriptResult = shell.evaluate(script);
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
		return scriptResult;
	}
}
