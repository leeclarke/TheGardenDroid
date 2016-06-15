import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.data.format.Formatters;
import akka.actor.Cancellable;
import format.AnnotationDateTimeFormatter;

public class Global extends GlobalSettings {
	public Cancellable scheduler;
	public static org.slf4j.Logger LocalLogger = Logger.underlying();
	
//	public <T extends EssentialFilter> Class<T>[] filters() {
//        return new Class[]{GzipFilter.class};
//    }
	
	@Override
	public void onStart(Application arg0) {
		
		Formatters.register(org.joda.time.DateTime.class, new AnnotationDateTimeFormatter());
		LocalLogger.warn("Global Startup called...");
		
		
	}
	

}
