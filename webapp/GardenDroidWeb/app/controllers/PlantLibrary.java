package controllers;

import java.util.List;

import models.Plant;
import models.PlantData;
import play.mvc.Controller;
import play.mvc.With;

public class PlantLibrary extends Controller {

	/**
	 * Load up plant Library for browse and search
	 */
	public static void viewPlantData() {
		
		List<PlantData> plants = PlantData.find("order By name").fetch();
		
		List<Plant> plantings = Plant.find("isActive = ?", true).fetch();
		render(plants,plantings);
	}
	
}
