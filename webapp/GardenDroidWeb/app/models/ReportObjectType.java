package models;

/**
 * Defines what Data Objects and fields can be used for running reports on, specifically for generating Charts. 
 * @author leeclarke
 */
public enum ReportObjectType {
	//TODO: Define Fields available for each Type and build select tag
	PLANT(new String[]{"",""}), 
	PLANTDATA(new String[]{"",""}) ,
	OBSERVATION(new String[]{"",""});
	
	public String objectFields[];
	public String name;
	
	ReportObjectType(String[] objectFields){
		this.name = this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
		this.objectFields = objectFields;
	}
}
