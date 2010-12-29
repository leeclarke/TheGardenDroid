package models;

import java.util.List;

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
	public boolean active;  //when inactive the item wont show up on the input form any longer. Its a reversible form of soft delete. 
//	public DataType type;

	public UserDataType(String name, String description) {
		this.name = name;
		this.description = description;
		this.active = true;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	public static List<UserDataType> fetchActiveDataTypes(){
		
	}
}

