package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * Logs a warning event which may coincide with an email notification if the option is enabled.
 * @author leeclarke
 */
@Entity
public class Warning extends Model {
	
	public Date dateTime;
	public String message;
    public boolean isActive;
    public AlertType alertType;
    
    public Warning(String message, boolean isActive, AlertType alertType){
    	this.dateTime = new Date();
    	this.message = message;
    	this.isActive = isActive;
    	this.alertType = alertType;
    }

    /**
     * Marks any Active Warnings of the given type to false
     * @param aType
     */
    public static void deactivateType(AlertType aType) {
		if(aType != null){
			List<Warning> activeList = getActive(aType);
			for (Warning warning : activeList) {
				warning.isActive = false;
				warning.save();
			}
		}		
	}
    
	/**
	 * Gets all active Warnings.
	 * @return 
	 */
	public static List<Warning> getActive() {
		return getActive(null);		
	}
	
	public static List<Warning> getActive(AlertType aType) {
		List<Warning> active;
		if(aType != null) {
			active = Warning.find("alertType = ? AND isActive = true order by dateTime desc" , aType).fetch();
		} else {
			active = Warning.find("isActive = true order by dateTime desc").fetch();
		}		
		return active;
	}
	
}
