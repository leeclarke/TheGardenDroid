package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;

import org.apache.log4j.Logger;

import play.Play;
import play.db.jpa.JPABase;
import play.db.jpa.Model;

@Entity
public class SensorRecordFrequency extends Model {
	private static Logger logger = Logger.getLogger(SensorRecordFrequency.class);
	public SensorType sensorType;
	public Integer frequencySeconds = 0;
	public Long lastPostTime = new Long(0);

	protected SensorRecordFrequency(SensorType sensorType, Integer frequencySeconds) {
		this.sensorType = (sensorType != null) ? sensorType : sensorType.INVALID;
		this.frequencySeconds = (frequencySeconds != null) ? frequencySeconds : new Integer(0);
	}

	/**
	 * The list of supported sensor types is set so instead of just accepting whatever we go to a default config.
	 * Essentially all Sensors have a Default record frequency to protect the DB from getting spammed. This only has to
	 * run once to create the values but will return what's int he DB if present.
	 * 
	 * @return
	 */
	public static List<SensorRecordFrequency> getInitSettings() {
		logger.debug("ENTER getInitSettings:");
		Integer defaultFrequency;
		try {
			defaultFrequency = new Integer(Play.configuration.getProperty("droid.default.sensor.frequency"));
		} catch (Exception e) {
			defaultFrequency = (15*60);//15 mins
		}
		List<SensorRecordFrequency> freqs = SensorRecordFrequency.findAll();
		if(freqs.size()==0) {
			for (int i = 0; i < SensorType.values().length; i++) {
				SensorType type = SensorType.values()[i];
				if(!type.isVirtual()) {
					SensorRecordFrequency newFreq = new SensorRecordFrequency(type, defaultFrequency);
					newFreq.save();
					freqs.add(newFreq);
				}
			}
		}
		return freqs;
	}

	/**
	 * Retrieves value for single sensor
	 * 
	 * @param type
	 * @return
	 */
	public static SensorRecordFrequency getByType(SensorType type) {
		return SensorRecordFrequency.find("sensorType = ?", type).first();
	}
	
	public static SensorRecordFrequency checkPosting(String sensorTypeString) {
		SensorRecordFrequency srf = SensorRecordFrequency.find("sensorType = ?", sensorTypeString).first();
	//TODO: Finish this!	
//		System.currentTimeMillis()
//		if()
		return srf;
	}

	/**
	 * Retrieves all ordered by SensorType
	 * 
	 * @return
	 */
	public static List<SensorRecordFrequency> getAllOrdered() {
		List resp = SensorRecordFrequency.find("order by sensorType desc").fetch();
		if(resp == null || resp.size()<1) {
			resp = SensorRecordFrequency.getInitSettings();
		}
		
		Collections.sort(resp,  new SensorRecordFrequencyComparator());
		return resp;
	}
}