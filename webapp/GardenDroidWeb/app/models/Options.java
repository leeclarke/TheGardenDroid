package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Options extends Model{
	public static final Integer ALIVE_DEFAULT = 60;
	public String email;
	public Boolean enableWarningNotification;
	public Boolean enableLowTempWarning;
	public Double  lowTempThreshold;
	public Boolean enableHighTempWarning;
	public Double  highTempThreshold;
	public Boolean enablePlantedWarnings;
	public Integer remoteAliveCheckMins;

	public Options(String email, Boolean enableWarningNotification, Boolean enableLowTempWarning, Double  lowTempThreshold, Boolean enableHighTempWarning, Double highTempThreshold, Boolean enablePlantedWarnings, Integer remoteAliveCheckMins) {
		this.email = email;
		this.enableWarningNotification = (enableWarningNotification == null)? false:enableWarningNotification;
		
		this.enableLowTempWarning = (enableLowTempWarning == null)? false:enableLowTempWarning;
		this.enableHighTempWarning = (enableHighTempWarning == null)? false:enableHighTempWarning;
		
		this.lowTempThreshold = (lowTempThreshold == null)? 0.0: lowTempThreshold;
		
		this.enablePlantedWarnings = (enablePlantedWarnings == null)? false:enablePlantedWarnings;
		this.highTempThreshold = (highTempThreshold == null)? 0.0: highTempThreshold;
		
		this.remoteAliveCheckMins = (remoteAliveCheckMins == null)?ALIVE_DEFAULT:remoteAliveCheckMins;
	}
}
