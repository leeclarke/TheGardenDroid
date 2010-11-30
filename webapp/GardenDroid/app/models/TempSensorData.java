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

}