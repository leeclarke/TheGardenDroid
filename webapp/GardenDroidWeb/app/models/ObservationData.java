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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * Observation data collected by the user about a given Planting.
 * @author leeclarke
 */
@Entity
public class ObservationData  extends Model {

	
	@ManyToOne
	public Plant plant;
	public Date dateCreated;
	@ManyToOne
	public UserDataType dataType;
	public Double dataValue;
	
	public ObservationData(Date dateCreated, Plant plant, UserDataType dataType, Double dataValue) {
		this.dateCreated = dateCreated;
		this.plant = plant;
		this.dataType = dataType;
		this.dataValue = dataValue;
	}
	
	public ObservationData(Plant plant, UserDataType dataType, Double dataValue) {
		this(new Date(), plant, dataType, dataValue);
	}
	
	/**
	 * Gets all observations related to a given plant
	 * @param planting
	 */
	public static List<ObservationData> retrieveObservationsForPlanting(Plant planting){
		List<ObservationData> resp = new ArrayList<ObservationData>();
		if(planting != null && planting.id != null) {
			resp = ObservationData.find("plant = ? order by dateCreated desc", planting).fetch();
		}
		return resp;
	}
}
