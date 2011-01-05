import java.util.List;

import play.*;
import play.db.jpa.JPABase;
import play.jobs.*;
import play.test.*;
 
import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {
    	//Load Default Observation fields:
        loadObservationFields();
        
        // Check if the database is empty
    	if(PlantData.count() == 0) {
            Fixtures.load("initial-data.yml");
        }
        
        
    }

	/**
	 * There are a couple of "User Defined Fields" that are not user defined but used by the system for alert checking and provide 
	 * default examples of UDFs.
	 */
	private void loadObservationFields() {
		List<UserDataType> types = UserDataType.findAll();
		if(types.size() != 0) {
			UserDataType uType = UserDataType.find("byName", UserDataType.DEFAULT_PLANT_IRRIGATION.name).first();
			if(!uType.active) {
				uType.active = true;
				uType.save();
			}
		}
		else {
			UserDataType.DEFAULT_PLANT_IRRIGATION.save();
		}
			
	}
 
}