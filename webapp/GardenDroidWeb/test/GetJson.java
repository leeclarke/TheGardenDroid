import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import models.TempSensorData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GetJson {
	public static void main(String[] args) throws IOException {
		StringWriter writer = new StringWriter();

	    Gson gson = new GsonBuilder().create();
	    Date now = new Date();

	    TempSensorData temp =  new TempSensorData(now, 78.23 , 22.79);
	    
	    gson.toJson("Hello", writer);
	    gson.toJson(temp, writer);
	    System.out.println(writer);
	  }
	
	//result=
	//{"tempF":78.23,"tempC":22.79,"dateTime":"Dec 1, 2010 5:16:12 PM","data":78.23,"sensorType":"TEMPERATURE"}
}
