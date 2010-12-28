package models;

import java.util.Date;

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
	
}
