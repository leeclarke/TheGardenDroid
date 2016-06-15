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
package model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.DateTime;

import play.db.ebean.Model;

//TODO: This Table has not been created yet. Need to investigate how this works first.

/**
 * Stores user defined scripts which are eval-ed real time to generate user defined reports.
 * @author leeclarke
 */
@Entity
@Table(name="user_report_scripts")
public class ReportUserScript extends Model {

	public static Finder<Long,ReportUserScript> find = new Finder<Long, ReportUserScript>(
		    Long.class, ReportUserScript.class
		  );
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long id;
	
	@Column(name = "date_created")
	public DateTime dateCreated;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "description")
	public String description;	
	
	@Column(name = "script")
	public String script;

	@Column(name = "start_date")
	public DateTime startDate;
	
	@Column(name = "end_date")
	public DateTime endDate;
	
	//TODO
	public Plant planting;
	
	public boolean activeOnlyPlantings;
	
	@Column(name = "report_type")
	public ReportType reportType;
	
	@Column(name = "report_field")
	public String reportField;
		
	public ReportUserScript(String name, String description, String script, DateTime startDate, DateTime endDate, Plant plant, boolean activeOnly, ReportType reportType, String reportField) {
		this.dateCreated = DateTime.now();
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
		return find.where().orderBy("name").findList();
	}
}
