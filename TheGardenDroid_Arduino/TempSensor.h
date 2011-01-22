/*
  TempSensor.h -Temprature sensor interface specificly for the DS1621 attached to the Arduino.
  Created by Lee Clarke, Oct 21, 2010.
  Released into the public domain.
*/
#include <Wire.h>
#ifndef TempSensor_h
#define TempSensor_h

#include "Sensor.h"

class TempSensor : public Sensor {
public:
  TempSensor(int sensorId, String name, unsigned long pollInterval);
  void startConversion(boolean start);
  int getSensorValue();
  int getSensorState();
  void tempThresholdTripped();
  int getHrTemp();
  int getTemp(byte reg);
  void setHighThresh(int tC);
  void setLowThresh(int tC);
  void setThresh(byte reg, int tC);
  void setConfig(byte cfg);
  String toString();
  byte sendStatus;
  boolean negTempC;
  double tempC;
  double tempF;
private:
  
 
};
#endif
