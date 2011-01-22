package models;

import java.util.Comparator;

/**
 * For some reason the order by doesn't sort asc correctly. Doing it in Java.
 * @author leeclarke
 */
public class SensorRecordFrequencyComparator implements Comparator<SensorRecordFrequency>{

	@Override
	public int compare(SensorRecordFrequency o1, SensorRecordFrequency o2) {
		if(o1.sensorType.getCode() > o2.sensorType.getCode()) {
			return 1;
		}
		else if(o1.sensorType.getCode() < o2.sensorType.getCode()){
			return -1;
		}				
		return 0;
	}
   
}
