import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class PlantModelTest extends UnitTest {

    @Test
	public void createAndRetrievePlant() {
		Fixtures.load("data.yml");


//TODO: Add PlantData to test!
	    // Create a new user and save it
	    Date now = new Date();
	    String name = "Broccoli";
	    String notes = "Test Log message.";
	    new Plant(now, name, notes, true, true).save();

	    Plant plant = Plant.find("byName", name).first();

	    // Test
	    assertNotNull(plant);

	    assertEquals(name, plant.name);
	    assertEquals(notes, plant.notes);
	    assertEquals(true, plant.isActive);
	    assertEquals(true, plant.isDroidFarmed);
	    assertEquals(now, plant.datePlanted);
	}



}
