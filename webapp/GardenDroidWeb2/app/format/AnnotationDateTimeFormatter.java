package format;

import java.text.ParseException;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import play.Logger;
import play.data.format.Formatters.AnnotationFormatter;

public class AnnotationDateTimeFormatter extends AnnotationFormatter<JodaDateTime, DateTime> {

    @Override
    public DateTime parse(JodaDateTime annotation, String text, Locale locale) throws ParseException {
    	Logger.warn("DateTimeFormatter Parse called...");
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        return DateTimeFormat.forPattern(annotation.pattern()).withLocale(locale).parseDateTime(text);
    }

    @Override
    public String print(JodaDateTime annotation, DateTime value, Locale locale) {
    	Logger.warn("DateTimeFormatter Print called...");
        if (value == null) {
            return null;
        }

        return value.toString(annotation.pattern(), locale);
    }
}