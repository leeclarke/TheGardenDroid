package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class SensorData extends Model {

    public Date dateTime;
    public double data;
    public SensorType sensorType;

    public SensorData(Date dateTime, double data, SensorType sensorType) {
        this.dateTime = dateTime;
        this.data = data;
        this.sensorType = sensorType;
    }

}