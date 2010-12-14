package models;

import java.util.Date;

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
	
}
