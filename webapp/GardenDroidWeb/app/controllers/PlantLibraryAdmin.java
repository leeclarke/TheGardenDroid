package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.groovy.tools.shell.commands.ShowCommand;

import models.ObservationData;
import models.Plant;
import models.PlantData;
import models.UserDataType;
import play.Play;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.JPABase;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

/**
 * Main controller for managing and interacting with plant Library data.
 * @author leeclarke
 */
@With(Secure.class)
public class PlantLibraryAdmin extends Controller {
	static Logger logger = Logger.getLogger(PlantLibraryAdmin.class);
	
	@Before
	static void addDefaults() {
	    renderArgs.put("appTitle", Play.configuration.getProperty("droid.title"));
	    renderArgs.put("appBaseline", Play.configuration.getProperty("droid.baseline"));
	}
	
	/**
	 * Load up plant Library for browse and search
	 */
	public static void viewPlantData(Integer count) {
		if(count == null || count <0)
			count = 10;
		List<PlantData> plants = PlantData.find("order By name").fetch(count);
		
		List<Plant> plantings = Plant.find("isActive = ?", true).fetch();
		render(plants,plantings);
	}
	
	public static void editPlantData(Long id) {
		logger.debug("ENTER editPlantData="+id);
		if(id != null && id >-1)	{
			PlantData plantData = PlantData.findById(id);
			render(plantData);
		} 
		render();
	}
	
	public static void editPlanted(Long id) {
		List<PlantData> plantData = PlantData.findAll();
		if(id != null && id >-1)	{
			Plant planted = Plant.findById(id);
			List<ObservationData> observations = ObservationData.retrieveObservationsForPlanting(planted);
			List<Plant> plantings = new ArrayList<Plant>();
			plantings.add(planted);
			List<UserDataType> activeUserTypes = UserDataType.fetchActiveDataTypes();
			
			render(planted,plantData,observations,activeUserTypes, plantings);
		} 
		
		render(plantData);
	}
	
	public static void postPlantData(Long Id,@Required(message = "Name can not be empty!") String name, String scientificName, String notes, @Required @Min(message = "Days Til Harvest must be > 0", value=1) int daysTillHarvest, int daysTillHarvestEnd, String sunlight, @Required @Min(message = "Low Temp should be >32", value=32) double lowTemp, double highTemp, @Required @Min(message = "Water Frequency must be > 0", value=1) int waterFreqDays){
		//check for -1 which indicates add
		logger.warn("ENTER POST");
		if(params._contains("DelPlant")){
			logger.warn("##### got DEL req");
			deletePlantData(Id);
		}
		
		PlantData plantData;
		if(Id == null || Id == -1 ) {
			plantData = new PlantData(name, scientificName, daysTillHarvest, daysTillHarvestEnd, sunlight, lowTemp, highTemp, waterFreqDays);
			if (validation.hasErrors()) {
				render("@editPlantData", plantData);
			}else {
				plantData.save();
			}			
		}
		else {
			plantData = PlantData.findById(Id);
			plantData.name = name;
			plantData.scientificName = scientificName;
			plantData.notes = notes;
			plantData.daysTillHarvest = daysTillHarvest;
			plantData.daysTillHarvestEnd = daysTillHarvestEnd;
			plantData.sunlight = sunlight;
			plantData.lowTemp = lowTemp;
			plantData.highTemp = highTemp;
			plantData.waterFreqDays = waterFreqDays;
			if (validation.hasErrors()) {
				render("@editPlantData", plantData);
			}else {
				plantData.save();
			}
		}
		PlantLibrary.viewPlantData();
	}
	
	/**
	 * @param Id - -1 or null value indicates an add.
	 * @param name
	 * @param datePlanted
	 * @param notes
	 * @param isActive
	 * @param isDroidFarmed
	 * @param plantCount
	 * @param harvestStart
	 * @param harvestEnd
	 * @param harvestYield
	 * @param plantDataId
	 */
	public static void postPlantedData(Long Id,@Required(message="Name is required.") String name, @Required(message="Date Planted is required.") Date datePlanted, String notes, boolean isActive, boolean isDroidFarmed, Integer plantCount, Date harvestStart, Date harvestEnd, Double harvestYield, @Required(message="Please select a Plant Type.") Long plantDataId){
		logger.info("ENTER postPlantedData");
		if(params._contains("deletePlnt")){
			logger.debug("##### got DEL req");
			deletePlanted(Id);
		}
		logger.debug("PLant ID = " + plantDataId);
		Plant planted;
		PlantData plantData = null;
		if(plantDataId != null && plantDataId >-1) {
			plantData = PlantData.findById(plantDataId);
			logger.warn("plantData == "+plantData.name);
		}
			
		if( Id == null || Id == -1) {
			if(datePlanted == null) datePlanted = new Date();
			planted = new Plant(datePlanted, name, notes, isActive, isDroidFarmed);
			planted.plantCount = plantCount;
			planted.harvestYield = harvestYield;
			planted.plantData = plantData;
			if(harvestStart != null) {
				planted.harvestStart =harvestStart;
			}
			else {
				planted.harvestStart = computeHarvestDate(planted.datePlanted, plantData);
			}
			if(harvestEnd != null) {
				planted.harvestEnd = harvestEnd;
			}
			else {
				planted.harvestEnd = computeHarvestDate(planted.datePlanted, plantData);
			}
			if (validation.hasErrors()) {
				logger.debug("Got Errors");
				logger.debug("ERRORS: "+validation.errorsMap());
				render("@editPlanted", planted);
		    } else {
		    	planted.save();
		    }
		} else {
			planted = Plant.findById(Id);
			if(planted != null)
			{	
				planted.name = name;
				logger.debug("datePlanted="+datePlanted );
				planted.datePlanted = datePlanted;
				planted.notes = notes;
				planted.isActive = isActive;
				planted.isDroidFarmed = isDroidFarmed;
				planted.plantCount = plantCount;
				planted.harvestYield = harvestYield;
				if(planted.plantData == null || planted.plantData.id != plantData.id)
				{
					if(harvestStart != null) {
						planted.harvestStart =harvestStart;
					}
					else {
						planted.harvestStart = computeHarvestDate(planted.datePlanted, plantData);
					}
					if(harvestEnd != null) {
						planted.harvestEnd = harvestEnd;
					}
					else {
						planted.harvestEnd = computeHarvestDate(planted.datePlanted, plantData);
					}
				}
				planted.plantData = plantData;
				logger.debug("harvestStart="+harvestStart );
				if (validation.hasErrors()) {
					logger.debug("Got Errors");
					logger.debug("ERR: "+validation.errorsMap());
					render("@editPlanted", planted);
			    } else {
			    	planted.save();
			    }
			} else {
				planted = new Plant(datePlanted, name, notes, isActive, isDroidFarmed);
				planted.plantCount = plantCount;
				planted.harvestYield = harvestYield;
				planted.harvestStart =harvestStart;
				planted.harvestEnd = harvestEnd;
				planted.plantData = plantData;
				validation.addError("Id", "Invalid ID save the item again to create a new Planting.", "");
				logger.debug("Got Errors");
				render("@editPlanted", planted);
			}
		}
		PlantLibrary.viewPlantData();
	}
	
	protected static Date computeHarvestDate(Date datePlanted, PlantData plantData) {
		Calendar plantingDate = Calendar.getInstance();
		plantingDate.setTime(datePlanted);
		plantingDate.add(Calendar.DATE, plantData.daysTillHarvestEnd);
		return plantingDate.getTime();
	}
	
	public static void deletePlanted(Long Id) {
		Plant planted = Plant.findById(Id);
		if(planted != null)
		{
			planted.delete();
		}
		PlantLibrary.viewPlantData();
	}
	
	public static void deletePlantData(Long Id) {
		PlantData plantData = PlantData.findById(Id);
		if(plantData != null)
		{
			plantData.delete();
		}
		PlantLibrary.viewPlantData();
	}
	
}
