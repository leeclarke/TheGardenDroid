package models;

/**
 * AlertType used by database as well as used by Alert messaging to reference canned messages.
 * @author leeclarke
 */
public enum AlertType {
	DROID_DOWN("GardenDroid Alert - Unavailable","The GardenDroid has not posted any sensor data in the alotted time and there for is considered to be off line. Check the Remote as soon as possible."),
	WATER_PUMP_DOWN("GardenDroid Alert -  Water Pump Dry","The GardenDroid's water pump system is reporting that it is out of water! Watering has been deactivated until the water level in the resavore rises."),
	LOW_TEMP_THRESHOLD("GardenDroid Alert -  LOW Temprature Warning","The GardenDroid has reported that the temprature has dropped below the global Low Temprature Threshold."),
	HIGH_TEMP_THRESHOLD("GardenDroid Alert -  HIGH Temprature Warning","The GardenDroid has reported that the temprature has risin abovethe global HIGH Temprature Threshold."),
	PLANT_TEMP_LOW("GardenDroid Alert -  ",""),
	PLANT_TEMP_HIGH("GardenDroid Alert -  ",""),
	PLANT_WATER_LOW("GardenDroid Alert -  ","");
	
	public String subject;
	public String message;

	AlertType(String subject, String message){
		this.subject = subject;
		this.message = message;
	}
}
