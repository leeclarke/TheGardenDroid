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

import play.db.jpa.*;
import play.db.jpa.GenericModel.JPAQuery;

@Entity
public class Plant extends Model {

    public Date datePlanted;
    public String name;
    public String notes;
    public boolean isActive;
    public Integer plantCount;
    public Date harvestStart;
    public Date harvestEnd;
    public Double harvestYield;
	public boolean isDroidFarmed;
	@ManyToOne
	public PlantData plantData;

	public Plant(){
		
	}
	
    public Plant(Date datePlanted, String name, String notes, boolean isActive, boolean isDroidFarmed) {
        this.datePlanted = datePlanted;
        this.name = name;
        this.notes = notes;
        this.isActive = isActive;
        this.isDroidFarmed = isDroidFarmed;
    }

    public Plant(Date datePlanted, String name, String notes) {
	        this.datePlanted = datePlanted;
	        this.name = name;
	        this.notes = notes;
	        this.isActive = false;
    }

	public static List<Plant> getActivePlantings() {
		List<Plant> planted = Plant.find("isActive = ? order by datePlanted desc", true).fetch();
		return planted;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[Plant]");
		sb.append("Name: ").append(this.name);
		sb.append("datePlanted: ").append(this.datePlanted);
		sb.append("isActive: ").append(this.isActive);
		sb.append("isDroidFarmed: ").append(this.isDroidFarmed);
		sb.append("plantData: ").append(this.plantData);
		return sb.toString();
	}
}