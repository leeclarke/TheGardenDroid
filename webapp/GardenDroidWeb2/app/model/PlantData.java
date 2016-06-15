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

import java.util.*;

import javax.persistence.*;

import org.joda.time.DateTime;

import play.db.ebean.Model;
import play.Logger;

@Entity
@Table(name="plant_data")
public class PlantData extends Model {

	private static org.slf4j.Logger  logger = Logger.underlying();
	
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "plant_data_id_seq" )
	public long id;
	
	@Column(name = "create_date")
    public DateTime created;
    
	@Column(name = "name")
    public String name;
    
	@Column(name = "scientific_name")
    public String scientificName;
    
	@Column(name = "notes")
    public String notes;
    
	@Column(name = "days_to_harvest")
    public int daysTillHarvest;
    
	@Column(name = "days_to_harvest_end")
    public int daysTillHarvestEnd;
    
	@Column(name = "sunlight")
    public String sunlight;
    
	@Column(name = "low_temp")
    public double lowTemp;
    
	@Column(name = "high_temp")
    public double highTemp;
    
	@Column(name = "water_frequency_days")
    public int waterFreqDays;
    
	@OneToMany(mappedBy="plantData", cascade=CascadeType.ALL)
	public List<Plant> plants;

    public PlantData(String name) {
		this.created = DateTime.now();
        this.name = name;
        this.plants = new ArrayList<Plant>();
    }

    public PlantData(String name, String scientificName, int daysTillHarvest, int daysTillHarvestEnd, String sunlight, double lowTemp, double highTemp, int waterFreqDays) {

		this.created = DateTime.now();
		this.name = name;
		this.scientificName = scientificName;
		this.daysTillHarvest = daysTillHarvest;
		this.daysTillHarvestEnd = daysTillHarvestEnd;
		this.sunlight = sunlight;
		this.lowTemp = lowTemp;
		this.highTemp = highTemp;
		this.waterFreqDays = waterFreqDays;
		this.plants = new ArrayList<Plant>();
    }

	public PlantData addPlant(DateTime datePlanted, String name, String notes, boolean isActive, boolean isDroidFarmed) {
	    Plant newPlant = new Plant(datePlanted, name, notes, isActive, isDroidFarmed);
	    newPlant.save();
	    
	    this.plants.add(newPlant);
	    this.save();
	    return this;
	}

	public PlantData addPlant(Plant newPlant) {
		if(newPlant != null)
		{
			newPlant.save();
			this.plants.add(newPlant);
		    this.save();
		}
		else {
			logger.warn("newPLant was NULL");
			System.out.println("newPLant was NULL");
		}
			
		return this;
		    
	}
}