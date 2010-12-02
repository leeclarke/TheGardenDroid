package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class TempSensorData extends SensorData {

    public double tempF;
    public double tempC;

    public TempSensorData(Date dateTime, double tempF, double tempC) {
        super(dateTime,tempF,SensorType.TEMPERATURE);
        this.tempF = tempF;
        this.tempC = tempC;
    }

	/**
	 * Helper method because it gets called more then once.
	 * @return
	 */
	public static TempSensorData getCurrentReading() {
		TempSensorData temp = TempSensorData.find("sensorType = ? order by dateTime desc", SensorType.TEMPERATURE).first();
		return temp;
	}

}