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

import java.util.*;

import javax.persistence.*;

import org.apache.log4j.Logger;

import play.db.jpa.*;

@Entity
public class PlantData extends Model {

	static Logger logger = Logger.getLogger(PlantData.class);
	
    public Date created;
    public String name;
    public String scientificName;
    public String notes;
    public int daysTillHarvest;
    public int daysTillHarvestEnd;
    public String sunlight;
    public double lowTemp;
    public double highTemp;
    public int waterFreqDays;
    
	@OneToMany(mappedBy="plantData", cascade=CascadeType.ALL)
	public List<Plant> plants;

    public PlantData(String name) {
		this.created = new Date();
        this.name = name;
        this.plants = new ArrayList<Plant>();
    }

    public PlantData(String name, String scientificName, int daysTillHarvest, int daysTillHarvestEnd, String sunlight, double lowTemp, double highTemp, int waterFreqDays) {

		this.created = new Date();
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

	public PlantData addPlant(Date datePlanted, String name, String notes, boolean isActive, boolean isDroidFarmed) {
	    Plant newPlant = new Plant(datePlanted, name, notes, isActive, isDroidFarmed).save();
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