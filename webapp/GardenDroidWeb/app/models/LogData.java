package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class LogData extends Model {

    public Date dateTime;
    public String message;
    public boolean isError;

    public LogData(Date dateTime, String message, boolean isError) {
        this.dateTime = dateTime;
        this.message = message;
        this.isError = isError;
    }

    public LogData(Date dateTime, String message) {
	        this.dateTime = dateTime;
	        this.message = message;
	        this.isError = false;
    }

}