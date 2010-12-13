package controllers;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

public class ReportsManager  extends Controller{
	
	@Before
	static void addDefaults() {
	    renderArgs.put("appTitle", Play.configuration.getProperty("droid.title"));
	    renderArgs.put("appBaseline", Play.configuration.getProperty("droid.baseline"));
	}
	
	/**
	 * General landing page for reports.
	 */
	public static void viewReports() {
		render();
	}
}
