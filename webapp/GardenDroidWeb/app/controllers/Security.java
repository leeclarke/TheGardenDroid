package controllers;

import models.User;

/**
 * Implements the security user auth against the DB. With out SSL enabled this would be pretty weak security but its
 * slightly better then nothing.
 * 
 * @author leeclarke
 */
public class Security extends Secure.Security {

	static boolean authenticate(String username, String password) {
		User user = User.find("byUsername", username).first();
		return user != null && user.password.equals(password);
	}

}
