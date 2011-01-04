package jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import models.AlertType;
import models.LogData;
import models.ObservationData;
import models.Options;
import models.Plant;
import models.PlantData;
import models.SensorData;
import models.SensorType;
import models.TempSensorData;
import models.UserDataType;
import models.Warning;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;
import play.libs.Mail;

@Every("1h")
public class WarningMonitorJob extends Job {
	static Logger logger = Logger.getLogger(WarningMonitorJob.class);

	/** 
	 * Job responsible for monitoring status of the GardenDroid and logging and sending out notifications if expected.
	 * @see play.jobs.Job#doJob()
	 */
	@Override
	public void doJob() throws Exception {
		logger.info("Starting WarningMonitorJob");
		super.doJob();
		Options options = Options.find("order by id").first();
		//See if should be active 
		
		boolean isOperational = verifyDroidIsOperational(options.remoteAliveCheckMins);
		if(!isOperational) {
			processAlert(options, AlertType.DROID_DOWN);
		} else { //Ensure that no Warnings are active.
			Warning.deactivateType(AlertType.DROID_DOWN);
		}
		
		int tempThresholdCheck = checkForTempThresholdsAlert(options);
		if(tempThresholdCheck == 1) {
			processAlert(options, AlertType.HIGH_TEMP_THRESHOLD);
		} else if(tempThresholdCheck == -1) {
			processAlert(options, AlertType.LOW_TEMP_THRESHOLD);
		} else { // OK returned, deactivate any previous Warnings.
			Warning.deactivateType(AlertType.HIGH_TEMP_THRESHOLD);
			Warning.deactivateType(AlertType.LOW_TEMP_THRESHOLD);
		}
		
		checkActivePlantings(options);
	}
	
	/**
	 * Sends Alert assuming rule checks pass, Might want to consider customizing the message in the future to contain more info.
	 * @param options
	 * @param aType
	 */
	protected boolean processAlert(Options options, AlertType aType ) {
		boolean emailSent = false;
		if(isAlertTypeActive(aType, options)) {
			try {
				if(options.enableWarningNotification) {
					sendNotification(options, aType.subject,aType.message);
					logger.debug("Email Notification Sent");
					emailSent = true;
				} else {
					logger.warn("Email Notifications currently disabled.");
				}
				new Warning(aType.message, true, aType).save();
			} catch (EmailException e) {
				logger.error("Email Alert failed.",e);
				new LogData(new Date(), "Failed to send email address to email address: "+options.email).save();
			}
		}
		return emailSent;
	}

	/**
	 * If the GardenDroid hasn't logged any data in over an hour then something is wrong!
	 * @return
	 */
	public boolean verifyDroidIsOperational(Integer aliveMins) {
		List<SensorData> latestData = SensorData.getSensorData(0,1);
		if(latestData.size()>0){
			SensorData lastData = latestData.get(0);
			Calendar now = Calendar.getInstance();
			now.add(Calendar.MINUTE, -(aliveMins));
			if(now.getTimeInMillis() < lastData.dateTime.getTime()){
				return true;
			}
		}		
		return false;
	}
	
	
	/**
	 * Just sends the notifications, all business process and error checking should happen elsewhere.
	 * @param options
	 * @param alertMessage
	 * @throws EmailException
	 */
	public void sendNotification(Options options, String subject, String alertMessage) throws EmailException{
		SimpleEmail email = new SimpleEmail();
		email.setFrom(Play.configuration.getProperty("mail.smtp.user"));
		email.addTo(options.email);
		email.setSubject(subject);
		email.setMsg(alertMessage);
		Mail.send(email); 
		logger.info("Email Alert sent to address:" + options.email + " subject:" + subject);
	}
	
	/**
	 * Using defaults or values set in options; check most recent temp readings for over/under threshold state.
	 * @param options
	 * @return - true value indicated Alert status.
	 */
	public int checkForTempThresholdsAlert(Options options) {
		int status = 0;
		TempSensorData tempReading = TempSensorData.getCurrentReading();
		logger.warn("current=="+tempReading);
		logger.warn("options=="+options);
		if(options.enableHighTempWarning && tempReading.tempF >= options.highTempThreshold ) {
			status = 1;
		} else if(options.enableLowTempWarning && tempReading.tempF <= options.lowTempThreshold){
			status = -1;
		}
		return status;
	}
	
	/**
	 * Using defaults or values set in options; check most recent temp readings for over/under threshold state.
	 * @param options
	 * @return - true value indicated Alert status.
	 */
	public int checkForTempThresholdsAlert(PlantData plant) {
		int status = 0;
		TempSensorData tempReading = TempSensorData.getCurrentReading();
		logger.warn("current=="+tempReading);
		logger.warn("planting=="+plant);
		if(tempReading.tempF >= plant.highTemp) {
			status = 1;
		} else if(tempReading.tempF <= plant.lowTemp){
			status = -1;
		}
		return status;
	}
	
	/**
	 * Check alert table to see if there is an active Warning of the given Alert type and that they are not older then the stale hours limit. 
	 * Active warnings are considered stale if they are older then the configured Stale hours options which defaults to 12.
	 * @param aType
	 * @return true if no active warnings or the active warning is over ignore limit time.
	 */
	public boolean isAlertTypeActive(AlertType aType, Options options) {
		List<Warning> resp = Warning.getActive(aType);
		if(resp.size()>0) {
			//See if warning has gone stale.
			Warning mostRecent = resp.get(0);
			Calendar now = Calendar.getInstance();
			now.add(Calendar.HOUR, -1*options.snoozeActiveWarnings_hours);
			Calendar lastDate = Calendar.getInstance();
			lastDate.setTime(mostRecent.dateTime);
			if(now.after(lastDate))
				return true;
		}
		return false;
	}

	/**
	 * All checks done on specific plants under the GardenDroid's care will be managed from here.
	 * @param options
	 * @throws EmailException 
	 */
	public boolean checkActivePlantings(Options options){
		boolean alertDetected = false;
		if(options.enablePlantedWarnings){
			StringBuilder sb = new StringBuilder();
			
			List<Plant> plantings = Plant.getActivePlantings();
			for (Plant plant : plantings) {
				//Check Temp Thresholds
				logger.warn("### Checking on plant: " + plant);
				int tempWarn = checkForTempThresholdsAlert(plant.plantData);
				if(tempWarn == 1){
					sb.append("\n ").append(plant.name).append(": Tempratures have exceeded the Plants indicated tolerance range.");
				} else if(tempWarn == -1) {
					sb.append("\n ").append(plant.name).append(": Tempratures have dropped below the Plants indicated tolerance range.");
				}
				//check watering
				HashMap<SensorType, SensorData> latest = SensorData.retrieveLatestSensorData();
				boolean sensorState = false; 
				boolean observationState = false;
				int waterDays = plant.plantData.waterFreqDays;
				if(latest.containsKey(SensorType.WATER_IRRIGATION) && latest.get(SensorType.WATER_IRRIGATION) != null) {
					
					SensorData water = latest.get(SensorType.WATER_IRRIGATION);
					logger.warn("### latest water data== " +water);
					Calendar waterDate = Calendar.getInstance();
					waterDate.setTime(water.dateTime);
					
					Calendar nextWaterDate = Calendar.getInstance();
					nextWaterDate.add(Calendar.DATE, waterDays);
					
					if(waterDate.before(nextWaterDate)){
						sensorState = true;
					}
				} 
				
				if(!sensorState){  //check observation entries.
					ObservationData mostRecent = ObservationData.find("plant = ? AND dataType = ? order by dateCreated desc", new Object[]{plant, UserDataType.DEFAULT_PLANT_IRRIGATION }).first();
					if(mostRecent != null) {
						Calendar obsWaterDate = Calendar.getInstance();
						obsWaterDate.setTime(mostRecent.dateCreated);
						
						Calendar nextWaterDate = Calendar.getInstance();
						nextWaterDate.add(Calendar.DATE, waterDays);
						if(obsWaterDate.before(nextWaterDate)){
							observationState = true;
						}
					}
				}
				logger.warn(" sensorState=" +sensorState + "  ObsState=" + observationState);
				if(!sensorState & !observationState) {
					sb.append("\n ").append(plant.name).append(": is due for irrigation.");
				}
				
			}
			
			if(sb.length()>0){
				try {
					alertDetected = true;
					if(options.enableWarningNotification) {
						sendNotification(options, "", sb.toString());
					}
					logger.info("Plant Alert Message Sent: "  + sb.toString());
				}catch (EmailException e) {
					logger.error("Email Alert failed.",e);
					new LogData(new Date(), "Failed to send email address to email address: "+options.email).save();
				}
			}
			
		}
		return alertDetected;
	}
}
