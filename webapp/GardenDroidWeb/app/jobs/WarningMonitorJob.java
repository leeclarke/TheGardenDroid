package jobs;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import models.AlertType;
import models.Options;
import models.SensorData;
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
			sendNotification(options, AlertType.DROID_DOWN.subject,AlertType.DROID_DOWN.message);
			//TODO: Log the error to the Log Table as an Alert.
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
		email.setFrom(Play.configuration.getProperty("mail.smtp.user"));//+"@gmail.com");
		email.addTo(options.email);
		email.setSubject(subject);
		email.setMsg(alertMessage);
		Mail.send(email); 
		logger.info("Email Alert sent to address:" + options.email + " subject:" + subject);
	}
}
