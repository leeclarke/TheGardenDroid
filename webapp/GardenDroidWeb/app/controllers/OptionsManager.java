package controllers;

import jobs.WarningMonitorJob;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;

import models.Options;
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
		
		render(options);
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
	public static void postOptions(Long id, @Required(message="Email is required for GardenDroid to be able to notify you.") String email,Boolean enableWarningNotification,Boolean enableLowTempWarning,Double  lowTempThreshold,Boolean enableHighTempWarning,Double  highTempThreshold,Boolean enablePlantedWarnings, Integer remoteAliveCheckMins) {
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
			options = new Options(email, enableWarningNotification, enableLowTempWarning, lowTempThreshold, enableHighTempWarning, highTempThreshold, enablePlantedWarnings, remoteAliveCheckMins);
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
}
