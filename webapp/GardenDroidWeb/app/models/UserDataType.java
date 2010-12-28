package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * User Defined data values relating to a Planting.
 * 
 * @author leeclarke
 */
@Entity
public class UserDataType extends Model {

	//public enum DataType {STRING, NUMBER};
	
	public String description;
	public String name;
//	public DataType type;

	public UserDataType(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}

