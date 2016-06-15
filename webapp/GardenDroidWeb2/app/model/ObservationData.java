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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.DateTime;

import play.Logger;
import play.db.ebean.Model;

import com.avaje.ebean.ExpressionList;

/**
 * Observation data collected by the user about a given Planting.
 * @author leeclarke
 */
@Entity
@Table(name="observation_data")
public class ObservationData  extends Model {
	private static org.slf4j.Logger  logger = Logger.underlying();
	
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "observation_data_id_seq" )
	public long id;
	
	@ManyToOne
	public Plant plant;
	
	@Column(name="date_created")
	public DateTime dateCreated;
	
	@ManyToOne
	public UserDataType dataType;
	
	@Column(name="data_value")
	public Double dataValue;
	
	public static Finder<Long,ObservationData> find = new Finder<Long, ObservationData>(
		    Long.class, ObservationData.class
		  );
	
	public ObservationData(DateTime dateCreated, Plant plant, UserDataType dataType, Double dataValue) {
		this.dateCreated = dateCreated;
		this.plant = plant;
		this.dataType = dataType;
		this.dataValue = dataValue;
	}
	
	public ObservationData(Plant plant, UserDataType dataType, Double dataValue) {
		this(DateTime.now(), plant, dataType, dataValue);
	}
	
	/**
	 * Gets all observations related to a given plant
	 * @param planting
	 */
	public static List<ObservationData> retrieveObservationsForPlanting(Plant planting){
		List<ObservationData> resp = new ArrayList<ObservationData>();
		if(planting != null && planting.id >0) {
			resp = find.where().eq("plant_id", planting.id).orderBy("date_created desc").findList();
		}
		return resp;
	}
	
	/**
	 * Gets all observations related to a given plant with-in date range
	 * @param planting
	 */
	public static List<ObservationData> retrieveObservationsForPlanting(Plant planting, DateTime startDate, DateTime endDate){
		List<ObservationData> resp = new ArrayList<>();
		if(planting != null && planting.id >0) {
			logger.warn("Get  query params: startDate="+startDate+"endDate="+endDate);
			
			ExpressionList<ObservationData> expression = find.where().eq("plant_id", planting.id);
			if(startDate != null)
			{
				expression = expression.gt("date_created", startDate);
			}
			if(endDate != null){
				expression = expression.lt("date_created", endDate);
			}
			
			return expression.orderBy("date_created").findList();
		}
		return resp;
	}
}
