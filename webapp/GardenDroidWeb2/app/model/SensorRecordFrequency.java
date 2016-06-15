package model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.Logger;
import play.Play;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
@Table(name="sensor_record_Frequency")
public class SensorRecordFrequency extends Model {
	private static org.slf4j.Logger  logger = Logger.underlying();
	
	@Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "sensor_record_frequency_id_seq" )
	public long id;
	
	@Column(name = "sensor_type")
	public SensorType sensorType;
	
	@Column(name = "frequency_seconds")
	public Integer frequencySeconds = 0;
	
	@Column(name = "last_post_time")
	public Long lastPostTime = new Long(0);
	
	public static Finder<Long,SensorRecordFrequency> find = new Finder<Long, SensorRecordFrequency>(
			Long.class, SensorRecordFrequency.class
	);

	protected SensorRecordFrequency(SensorType sensorType, Integer frequencySeconds) {
		this.sensorType = (sensorType != null) ? sensorType : sensorType.INVALID;
		this.frequencySeconds = (frequencySeconds != null) ? frequencySeconds : new Integer(0);
	}

	/**
	 * The list of supported sensor types is set so instead of just accepting whatever we go to a default config.
	 * Essentially all Sensors have a Default record frequency to protect the DB from getting spammed. This only has to
	 * run once to create the values but will return what's in the DB if present.
	 * 
	 * @return
	 */
	public static List<SensorRecordFrequency> getInitSettings() {
		logger.debug("ENTER getInitSettings:");
		Integer defaultFrequency;
		try {
			defaultFrequency = Play.application().configuration().getInt("droid.default.sensor.frequency");
		} catch (Exception e) {
			defaultFrequency = (15*60);//15 mins
		}
		List<SensorRecordFrequency> freqs = find.all();
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
		return find.where().eq("sensorType", type).findUnique();
	}
	
	public static SensorRecordFrequency checkPosting(String sensorTypeString) {
		SensorType type = SensorType.valueOf(sensorTypeString);

		return getByType(type);
	}

	/**
	 * Retrieves all ordered by SensorType
	 * 
	 * @return
	 */
	public static List<SensorRecordFrequency> getAllOrdered() {
		List<SensorRecordFrequency> resp = find.where().orderBy("sensorType asc").findList();
		if(resp == null || resp.size()<1) {
			resp = SensorRecordFrequency.getInitSettings();
		}
		
//		Collections.sort(resp,  new SensorRecordFrequencyComparator());
		return resp;
	}
}