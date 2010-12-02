package model;
import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;
import org.apache.log4j.Logger;

public class PlantDataModelTest extends UnitTest {

	static Logger logger = Logger.getLogger(PlantDataModelTest.class);
	
    @Test
	public void createAndRetrievePlant() {
	    // Create a new user and save it
	    Date now = new Date();
	    String name = "Broccoli";
	    String scientificName = "Brassica Oleracea";
	    int daysTillHarvest = 42;
	    int daysTillHarvestEnd = 56;
	    String sunlight = "full sun";
	    double lowTemp = 44.0;
	    double highTemp = 82.0;
	    int waterFreqDays = 5;

	    new PlantData(name, scientificName, daysTillHarvest, daysTillHarvestEnd, sunlight, lowTemp, highTemp, waterFreqDays).save();

	    PlantData plantData = PlantData.find("byName", name).first();

	    // Test
	    assertNotNull(plantData);
		assertNotNull(plantData.created);
	    assertEquals(name, plantData.name);
	    assertEquals(scientificName, plantData.scientificName);
	    assertEquals(daysTillHarvest, plantData.daysTillHarvest);
	    assertEquals(daysTillHarvestEnd, plantData.daysTillHarvestEnd);
	    assertEquals(sunlight, plantData.sunlight);
	    assertEquals(lowTemp, plantData.lowTemp,0);
	    assertEquals(highTemp, plantData.highTemp,0);
	    assertEquals(waterFreqDays, plantData.waterFreqDays);

	}

	@Test
	public void addPlant() {
		logger.debug("");
		String name = "Broccoli";
		String plantingName = "Winter Broc";
		PlantData plantData = PlantData.find("byName", name).first();
		assertNotNull(plantData);
		
		plantData.addPlant(new Date(), plantingName, "notes", true, true);
		
		//Go get it
		Plant wBroc = Plant.find("byName", plantingName).first();
		assertNotNull(wBroc);
	}

	@Test
	public void addPlant_Object() {
		logger.debug("add Plant");
		String name = "Broccoli";
		String plantingName = "Winter Broc";
		PlantData plantData = PlantData.find("byName", name).first();
		assertNotNull(plantData);
		
		Plant newPlanting = new Plant(new Date(), plantingName, "Some notes",true,true);
		assertNotNull(newPlanting);
		
		plantData.addPlant(newPlanting);
		
		//Go get it
		Plant wBroc = Plant.find("byName", plantingName).first();
		assertNotNull(wBroc);
		assertEquals(plantingName, wBroc.name);
	}
}
