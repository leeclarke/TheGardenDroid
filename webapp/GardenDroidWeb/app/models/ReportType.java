package models;

/**
 * Defines the types of report results available for display.
 * @author leeclarke
 */
public enum ReportType {
	/**
	 * Raw script output which should be a String result and can be HTML or other renderable result. 
	 */
	SCRIPT, 
	
	/**
	 * sparkline Chart result 
	 */
	CHART,
	
	/**
	 * Paginated table like other data tables in the app. 
	 */
	TABLE;
	
	public String name = ""; 
	
	ReportType(){
		this.name = this.name();
	}
}
