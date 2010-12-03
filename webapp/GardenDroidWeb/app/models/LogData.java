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

    public static List getErrors() {
    	List<LogData> errList = new ArrayList<LogData>();
    	errList = LogData.find("isError = ? order by dateTime desc", true).fetch();
    	return errList;
	}
    
    public static List getLogEntries() {
    	List<LogData> logList = new ArrayList<LogData>();
    	logList = LogData.find("isError = ? order by dateTime desc", false).fetch();
    	return logList;
	}
}