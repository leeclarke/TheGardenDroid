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
