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
package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;

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
	@Lob
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
