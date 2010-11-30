import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class PlantDataModelTest extends UnitTest {

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


	//TODO: Add test for getting planted
	@Test
	public void addPlant() {
		fail("implement test");
	}

	@Test
	public void addPlant_Object() {
		fail("implement test");
	}
}
