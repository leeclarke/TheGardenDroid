package utils;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class JodaDateDeserializer extends JsonDeserializer<DateTime>{

	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy");
	@Override
	public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        String dateTimeString = node.textValue();
        if(dateTimeString == null || dateTimeString.isEmpty()){
        	return null;
        }
		return formatter.parseDateTime(dateTimeString);  
	}

}
