package controllers;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.groovy.tools.shell.commands.ShowCommand;

import models.Plant;
import models.PlantData;
import play.data.validation.Min;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class PlantLibraryAdmin extends Controller {
	static Logger logger = Logger.getLogger(PlantLibraryAdmin.class);
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
		if(id != null && id >-1)	{
			Plant planted = Plant.findById(id);
			render(planted);
		} 
		render();
	}
	
	public static void postPlantData(Long Id,@Required(message = "Name can not be empty!") String name, String scientificName, String notes, @Required @Min(message = "Days Til Harvest must be > 0", value=1) int daysTillHarvest, int daysTillHarvestEnd, String sunlight, @Required @Min(message = "Low Temp should be >32", value=32) double lowTemp, double highTemp, @Required @Min(message = "Water Frequency must be > 0", value=1) int waterFreqDays){
		//check for -1 which indicates add
		
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
	
	public static void postPlantedData(Long Id,@Required(message="Name is required.") String name, @Required(message="Date Planted is required.") Date datePlanted, String notes, boolean isActive, boolean isDroidFarmed, Integer plantCount, Date harvestStart, Date harvestEnd, Double harvestYield){
		//check for -1 which indicates add
		logger.warn("ENTER postPlantedData");
		Plant planted;
		if(Id == -1 || Id == null ) {
			planted = new Plant(datePlanted, name, notes, isActive, isDroidFarmed);
			planted.plantCount = plantCount;
			planted.harvestYield = harvestYield;
			if(harvestStart != null) planted.harvestStart =harvestStart;
			if(harvestEnd != null) planted.harvestEnd = harvestEnd;
			if (validation.hasErrors()) {
				logger.warn("Got Errors");
				logger.warn("ERRORS: "+validation.errorsMap());
				render("@editPlanted", planted);
		    } else {
		    	planted.save();
		    }
		} else {
			planted = Plant.findById(Id);
			if(planted != null)
			{	
				planted.name = name;
				logger.warn("datePlanted="+datePlanted );
				planted.datePlanted = datePlanted;
				planted.notes = notes;
				planted.isActive = isActive;
				planted.isDroidFarmed = isDroidFarmed;
				planted.plantCount = plantCount;
				planted.harvestYield = harvestYield;
				logger.warn("harvestStart="+harvestStart );
				if(harvestStart != null ) planted.harvestStart =harvestStart;
				if(harvestEnd != null) planted.harvestEnd = harvestEnd;
				if (validation.hasErrors()) {
					logger.warn("Got Errors");
					logger.warn("ERR: "+validation.errorsMap());
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
				validation.addError("Id", "Invalid ID save the item again to create a new Planting.", "");
				logger.warn("Got Errors");
				render("@editPlanted", planted);
			}
		}
		PlantLibrary.viewPlantData();
	}
}
