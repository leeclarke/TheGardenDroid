package jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import models.AlertType;
import models.LogData;
import models.Options;
import models.SensorData;
import models.TempSensorData;
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
		super.doJob();
		Options options = Options.find("order by id").first();
		
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
	protected void processAlert(Options options, AlertType aType ) {
		if(!isAlertTypeActive(aType, options)) {
			try {
				sendNotification(options, aType.subject,aType.message);
				new Warning(aType.message, true, aType).save();
			} catch (EmailException e) {
				logger.error("Email Alert failed.",e);
				new LogData(new Date(), "Failed to send email address to email address: "+options.email).save();
			}
		}
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
	 */
	public void checkActivePlantings(Options options) {
		// TODO Auto-generated method stub
		
	}
}
