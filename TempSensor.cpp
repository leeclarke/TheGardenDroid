/**
 * TempSensor.cpp -Temprature sensor interface specificly for the DS1621 attached to the Arduino.
 * Created by Lee Clarke, Oct 21, 2010.
 * Released into the public domain.
 */
#include "TempSensor.h"
#include "PollEvent.h"
#include "Sensor.h"
#include <Wire.h> 

TempSensor::TempSensor(int sensorId, String name, unsigned long pollInterval)
  :Sensor(sensorId, name, pollInterval){
}

/**
 * Retrieves the Temp value if it is time to execute a polling request.
 * 
 * Return 1 for success, 0 if data was not due for update, -1 for fail.
 */
int TempSensor::getSensorValue() {
  int resp = -1;
  return resp;
}

int TempSensor::getSensorState(){
  int resp = 0;
  return resp; 
}
