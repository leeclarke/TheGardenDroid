package jobs;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import models.ObservationData;
import models.Options;
import models.Plant;
import models.TempSensorData;
import models.UserDataType;
import play.mvc.Before;
import play.test.Fixtures;
import util.BaseUnitTest;

public class WarningMonitorJob_PlantingTest  extends BaseUnitTest {

	Options options = new Options("lee.k.clarke@gmail.com", true,true,new Double(33),true,new Double(95),true,new Integer(60));
	
	@Before
	public void setUp() {
	    Fixtures.deleteAll();
	    Fixtures.load("initial-data.yml");
	}
	
	
	@Test
	public void checkActivePlantings() {
		
		//create nice Temp value that will fail.
		new TempSensorData(new Date(),100.0,35.0).save();
		
		WarningMonitorJob warning =  new WarningMonitorJob();
		boolean resp = warning.checkActivePlantings(options);
		assertTrue(resp);
		
	}
	
	/**
	 * No alert should be sent because all is with-in ranges.
	 */
	@Test
	public void checkActivePlantingsAllOK() {
		
		//create observation entry to prevent water alert and test that process.
		//create watering for today for all
		List<Plant> plantings = Plant.getActivePlantings();
		for (Plant plant : plantings) {
			new ObservationData(plant, UserDataType.DEFAULT_PLANT_IRRIGATION, 1.0).save();
		}
		//create nice Temp value that will pass.
		new TempSensorData(new Date(),70.0,23.0).save();
		
		
		
		WarningMonitorJob warning =  new WarningMonitorJob();
		boolean resp = warning.checkActivePlantings(options);
		assertTrue("Water test should have passed but failed.",!resp);
		
	}

}
