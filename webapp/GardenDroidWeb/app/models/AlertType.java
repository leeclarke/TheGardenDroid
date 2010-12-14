package models;

/**
 * AlertType used by database as well as used by Alert messaging to reference canned messages.
 * @author leeclarke
 */
public enum AlertType {
	DROID_DOWN("GardenDroid Unavailable","The GardenDroid has not posted any sensor data in the alotted time and there for is considered to be off line. Check the Remote as soon as possible."),
	WATER_PUMP_DOWN("GardenDroid Water Pump Dry","The GardenDroid's water pump system is reporting that it is out of water! Watering has been deactivated until the water level in the resavore rises.");
	
	
	public String subject;
	public String message;

	AlertType(String subject, String message){
		this.subject = subject;
		this.message = message;
	}
}
