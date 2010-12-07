package controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.groovy.tools.shell.commands.ShowCommand;

import models.Plant;
import models.PlantData;
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
		PlantData plantData = new PlantData("");
		logger.error("ENTER editPlantData="+id);
		if(id != null && id >-1)	{
			plantData = PlantData.findById(id);
			if(plantData == null)
				logger.error("Recieved Null when looking up plantdata id="+id);
		} else {
			plantData.id = (long) -1;
		}
		render(plantData);
	}
	
	public static void editPlanted(Long id) {
		Plant planted = new Plant();
		if(id != null && id >-1)
		{
			planted = Plant.findById(id);
		} else {
			planted.id = (long) -1;
		}
		render(planted);
	}
	
	public static void postPlantData(Long Id,String name, String scientificName, String notes, int daysTillHarvest, int daysTillHarvestEnd, String sunlight, double lowTemp, double highTemp, int waterFreqDays){
		//check for -1 which indicates add
		PlantData plantData;
		if(Id == -1 || Id == null ) {
			plantData = new PlantData(name, scientificName, daysTillHarvest, daysTillHarvestEnd, sunlight, lowTemp, highTemp, waterFreqDays).save();
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
			plantData.save();
		}
		
		render(plantData);
	}
	
	public static void postPlantedData(){
		//check for -1 which indicates add
		
	}
}
