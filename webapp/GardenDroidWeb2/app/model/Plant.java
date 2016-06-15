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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.joda.time.DateTime;

import play.db.ebean.Model;

@Entity
@Table(name="plants")
public class Plant extends Model {
	public static Finder<Long,Plant> find = new Finder<Long, Plant>(
		    Long.class, Plant.class
		  );
	
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "plant_id_seq" )
	public long id;
	
	@Column(name = "date_planted")
    public DateTime datePlanted;
    
	@Column(name = "name")
    public String name;
    
	@Column(name = "notes")
    public String notes;
    
	@Column(name = "is_active")
    public boolean isActive;
    
	@Column(name = "plant_count")
    public Integer plantCount;
    
	@Column(name = "harvest_start_date")
    public DateTime harvestStart;
    
	@Column(name = "harvest_end_date")
    public DateTime harvestEnd;
	
	@Column(name = "is_droid_farmed")
    public boolean isDroidFarmed;
	
    @ManyToOne
	public PlantData plantData;
	
    @OneToMany
	public List<ObservationData> observations;
	
	public Plant(){
		
	}
	
    public Plant(DateTime datePlanted, String name, String notes, boolean isActive, boolean isDroidFarmed) {
        this.datePlanted = datePlanted;
        this.name = name;
        this.notes = notes;
        this.isActive = isActive;
        this.isDroidFarmed = isDroidFarmed;
    }

    public Plant(DateTime datePlanted, String name, String notes) {
	        this.datePlanted = datePlanted;
	        this.name = name;
	        this.notes = notes;
	        this.isActive = false;
    }

	public static List<Plant> getActivePlantings() {
		List<Plant> planted = find.where().eq("isActive", true).orderBy("date_planted desc").findList();
		return planted;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[Plant]");
		sb.append("ID: ").append(this.id);
		sb.append("Name: ").append(this.name);
		sb.append("datePlanted: ").append(this.datePlanted);
		sb.append("isActive: ").append(this.isActive);
		sb.append("isDroidFarmed: ").append(this.isDroidFarmed);
		sb.append("plantData: ").append(this.plantData);
		return sb.toString();
	}
}