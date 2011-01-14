package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * Stores user defined scripts which are eval-ed real time to generate user defined reports.
 * @author leeclarke
 */
@Entity
public class ReportUserScript extends Model {

	public Date dateCreated;
	public String name;
	public String description;
	public String script;
	public Date startDate;
	public Date endDate;
	public Plant planting;
	public boolean activeOnlyPlantings;
	public ReportType reportType;
	public String reportField;
	
	public ReportUserScript(String name, String description, String script, Date startDate, Date endDate, Plant plant, boolean activeOnly, ReportType reportType, String reportField) {
		this.dateCreated = new Date();
		this.name = name;
		this.description = description;
		this.script = script;
		this.startDate = startDate;
		this.endDate = endDate;
		this.planting = plant;
		this.reportType = (reportType == null)? ReportType.SCRIPT:reportType;
		this.reportField = reportField;
	}
	
	public static List<ReportUserScript> fetchAllScripts() {
		return ReportUserScript.find("order by name").fetch();
	}
}
