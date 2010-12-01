package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Log extends Model {

    public Date dateTime;
    public String message;
    public boolean isError;

    public Log(Date dateTime, String message, boolean isError) {
        this.dateTime = dateTime;
        this.message = message;
        this.isError = isError;
    }

    public Log(Date dateTime, String message) {
	        this.dateTime = dateTime;
	        this.message = message;
	        this.isError = false;
    }

}