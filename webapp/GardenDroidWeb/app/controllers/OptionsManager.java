/**
 * The GardenDroid, a self monitoring and reporting mini-greenhouse.
 *
 * Copyright (c) 2010-2011 Lee Clarke
 *
 * LICENSE:
 *
 * This file is part of TheGardenDroid (https://github.com/leeclarke/TheGardenDroid).
 *
 * TheGardenDroid is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any
 * later version.
 *
 * TheGardenDroid is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with TheGardenDroid.  If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
package controllers;

import java.util.List;

import jobs.WarningMonitorJob;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import models.Options;
import models.UserDataType;
import play.Play;
import play.data.validation.Required;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

/**
 * Manages configuration Options for GardenDroid
 * @author leeclarke
 */
@With(Secure.class)
public class OptionsManager  extends Controller{
	static Logger logger = Logger.getLogger(OptionsManager.class);
	
	@Before
	static void addDefaults() {
	    renderArgs.put("appTitle", Play.configuration.getProperty("droid.title"));
	    renderArgs.put("appBaseline", Play.configuration.getProperty("droid.baseline"));
	}
	
	/**
	 * Display page
	 */
	public static void viewOptions() {
		Options options = Options.find("order by id").first();
		List<UserDataType> userDataFields = UserDataType.find("order by name").fetch();
		logger.warn("UserDataType count=" + userDataFields.size());
		render(options, userDataFields);
	}
	
	/**
	 * Sends test email to verify email address and warning system is working.
	 * @param options
	 */
	public static void sendTestEmail(Options options) {
		WarningMonitorJob warning = new WarningMonitorJob();
		boolean emailFailed = false;
		try {
			warning.sendNotification(options, "GardenDroid Test Alert", "This is only a test, had it been an actual emergency you most definitely would have been told exactly where to go...  ;)  Oh good news, if your readign this then the notification system is working right!");
		} catch(EmailException ee) {
			emailFailed = true;
			logger.error("Error sending email: ", ee);
		}		
		
		render(options, emailFailed);
	}
	
	/**
	 * Post responds to the post event as well as the test email button which saves before sending email.
	 * @param id
	 * @param email
	 * @param enableWarningNotification
	 * @param enableLowTempWarning
	 * @param lowTempThreshold
	 * @param enableHighTempWarning
	 * @param highTempThreshold
	 * @param enablePlantedWarnings
	 * @param remoteAliveCheckMins
	 */
	public static void postOptions(Long id, @Required(message="Email is required for GardenDroid to be able to notify you.") String email,Boolean enableWarningNotification,Boolean enableLowTempWarning,Double  lowTempThreshold,Boolean enableHighTempWarning,Double  highTempThreshold,Boolean enablePlantedWarnings, Integer remoteAliveCheckMins, Integer snoozeActiveWarnings_hours) {
		Options options = Options.find("order by id").first();
		validation.email(email);
		if(enableLowTempWarning != null)
			validation.required(lowTempThreshold).message("Must provide a temp value to check against.");
		if(enableHighTempWarning != null)
			validation.required(highTempThreshold).message("Must provide a temp value to check against.");
		
		if(enableLowTempWarning != null && enableHighTempWarning != null) { //ensure high is above low.
			if(lowTempThreshold != null && highTempThreshold != null &&  (highTempThreshold < lowTempThreshold)) {
				validation.addError("highTempThreshold", "The High Threshold should exceed the Low Threshold.");
				validation.addError("lowTempThreshold", "The Low Threshold should not exceed the High Threshold.");
			}
		}
		
		//Always get the first one as there shouldn't be multiple entries.
		if(options == null){
			options = new Options(email, enableWarningNotification, enableLowTempWarning, lowTempThreshold, enableHighTempWarning, highTempThreshold, enablePlantedWarnings, remoteAliveCheckMins, snoozeActiveWarnings_hours);
		}
		else {
			options.email = email;
			options.enableWarningNotification = (enableWarningNotification == null)?false:enableWarningNotification;
			options.enablePlantedWarnings = (enablePlantedWarnings == null)?false:enablePlantedWarnings;
			options.enableLowTempWarning = (enableLowTempWarning == null)?false:enableLowTempWarning;
			options.enableHighTempWarning = (enableHighTempWarning == null)?false:enableHighTempWarning;
			options.lowTempThreshold = (lowTempThreshold == null)?0.0:lowTempThreshold;
			options.highTempThreshold = (highTempThreshold == null)?0.0:highTempThreshold;
			options.remoteAliveCheckMins = (remoteAliveCheckMins == null)?Options.ALIVE_DEFAULT:remoteAliveCheckMins;
			options.snoozeActiveWarnings_hours = (snoozeActiveWarnings_hours == null)?Options.SNOOZE_DEFAULT:snoozeActiveWarnings_hours;
		}
		if(validation.hasErrors()) {
			logger.warn("Val Err= "+validation.errorsMap());
			render("@viewOptions", options);
		}
		options.save();
		if(params._contains("testemail")) {
			OptionsManager.sendTestEmail(options);
		}
		OptionsManager.viewOptions();	
	}
	
	public static void postUserData(Long id, @Required(message="field name is required")String name, String description, boolean active) {
		id = (id == null)?-1:id;
		
		UserDataType uType = UserDataType.findById(id);
		if(uType == null) {
			uType = new UserDataType(name, description);
		}	
		else {
			uType.name = name;
			uType.description = description;
			uType.active = active;
		}
		uType.save();
		OptionsManager.viewOptions();
	}
}
