package controllers;

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

	@Before
	static void addDefaults() {
	    renderArgs.put("appTitle", Play.configuration.getProperty("droid.title"));
	    renderArgs.put("appBaseline", Play.configuration.getProperty("droid.baseline"));
	}
	
	public static void viewOptions() {
		Options options = Options.find("order by id").first();
		render(options);
	}
	
	public static void postOptions(Long id, @Required(message="Email is required for GardenDroid to be able to notify you.") String email,Boolean enableWarningNotification,Boolean enableLowTempWarning,Double  lowTempThreshold,Boolean enableHighTempWarning,Double  highTempThreshold,Boolean enablePlantedWarnings) {
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
			options = new Options(email, enableWarningNotification, enableLowTempWarning, lowTempThreshold, enableHighTempWarning, highTempThreshold, enablePlantedWarnings);
		}
		else {
			options.email = email;
			options.enableWarningNotification = (enableWarningNotification == null)?false:enableWarningNotification;
			options.enablePlantedWarnings = (enablePlantedWarnings == null)?false:enablePlantedWarnings;
			options.enableLowTempWarning = (enableLowTempWarning == null)?false:enableLowTempWarning;
			options.enableHighTempWarning = (enableHighTempWarning == null)?false:enableHighTempWarning;
			options.lowTempThreshold = (lowTempThreshold == null)?0.0:lowTempThreshold;
			options.highTempThreshold = (highTempThreshold == null)?0.0:highTempThreshold;
		}
		if(validation.hasErrors()) {
			render("@viewOptions", options);
		}
		options.save();
		OptionsManager.viewOptions();	
	}
}
