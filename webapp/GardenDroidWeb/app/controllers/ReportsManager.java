package controllers;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.MissingPropertyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;

import models.ObservationData;
import models.Plant;
import models.PlantData;
import models.ReportType;
import models.ReportUserScript;
import models.SensorData;
import models.SensorType;
import models.TempSensorData;
import models.UserDataType;
import play.Play;
import play.data.validation.Required;
import play.db.jpa.JPABase;
import play.exceptions.CompilationException;
import play.mvc.Before;
import play.mvc.Controller;

/**
 * Central controller for viewing Reports.
 * @author leeclarke
 */
public class ReportsManager  extends Controller{
	static Logger logger = Logger.getLogger(ReportsManager.class);
	
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
	 * Supports Edit form
	 * @param id
	 */
	public static void editUserScript(Long id) {
		List<Plant> plantings = Plant.find("isActive = ?", true).fetch();
		List<ReportType> reportTypes = Arrays.asList(ReportType.values());
		if(id != null) {
			ReportUserScript script = ReportUserScript.findById(id);
			if(script != null)
				render(plantings,script, reportTypes);
		}
		
		render(plantings, reportTypes);
		
	}
	
	public static void saveUserScript(Long id, @Required(message="Must provide a name.") String name, String description, @Required(message="You need to provide a script!") String scriptBody, Date startDate, Date endDate, Long plantDataId, boolean activeOnlyPlantings, ReportType reportType, String reportField) {
		logger.warn("##### ENTER save id="+id);
		if(params._contains("deleteScript")){
			logger.warn("##### got DEL req");
			deleteUserScript(id);
		}
		ReportUserScript script;
		Plant plant = null;
		if(plantDataId != null && plantDataId >-1)
			plant = Plant.findById(plantDataId);
		if(id != null && id > -1) {
			logger.warn("Saving id="+id);
			script = ReportUserScript.findById(id);
			logger.warn("Got script, do update");
			if(script != null) {
				script.name = name;
				script.description = description;
				script.script = scriptBody;
				script.planting = plant;
				script.activeOnlyPlantings = activeOnlyPlantings;
				script.reportType = (reportType == null)? ReportType.SCRIPT: reportType;
				script.reportField = reportField;
				logger.warn("reportType == "+ reportType);
				if(startDate != null) script.startDate = startDate;
				if(endDate != null) script.endDate = endDate;
				
				script.save();
			}
			else {
				script = new ReportUserScript(name, description, scriptBody, startDate, endDate, plant, activeOnlyPlantings, reportType, reportField).save();
			}
		}
		else
		{
			script  = new ReportUserScript(name, description, scriptBody, startDate, endDate, plant, activeOnlyPlantings, reportType, reportField).save();
		}
		logger.warn("POST id="+script.id);
		ReportsManager.viewReports();		
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
				//TODO: Ensure that the generic Data field gets populated with tempF
				for (SensorData sd : sensorData) {
					if(sd.sensorType == SensorType.TEMPERATURE) {
						
						sd.data = ((TempSensorData)sd).tempF;
					}
				}
			}
			
			
			
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
			GroovyShell shell = new GroovyShell(binding);
			
			try {
				scriptResult = shell.evaluate(script.script);
			} catch(CompilationFailedException e) {
				scriptResult = e.getMessage();
			} catch (MissingPropertyException m) {
				scriptResult = m.getMessage();
			} catch (NullPointerException n) {
				scriptResult = "Sorr, the script was null or invalid, edit the script and try again.";
			}
		}
		else {
			script = new ReportUserScript("InvalidScript","","",null,null,null,false, null,null);
			scriptResult = "Sorr, the script was null or invalid, edit the script and try again.";
		}
		render(script, scriptResult);
	}
	
	public static void deleteUserScript(Long id) {
		ReportUserScript resp = ReportUserScript.findById(id);
		if(resp != null)
			resp.delete();
		
		ReportsManager.viewReports();
	}
	
}
