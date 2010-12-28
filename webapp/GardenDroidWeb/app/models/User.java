package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class User  extends Model {
	
	public String userName;
	public String password;

	public User(){
		
	}
	
	public User(String userName, String password){
		this.password = password;
		this.userName = userName;
	}
}
