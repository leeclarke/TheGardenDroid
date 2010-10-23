/*
  TempSensor.h -Temprature sensor interface specificly for the DS1621 attached to the Arduino.
  Created by Lee Clarke, Oct 21, 2010.
  Released into the public domain.
*/
#include <Wire.h>
#ifndef TempSensor_h
#define TempSensor_h

#include "PollEvent.h"
#include "Sensor.h"

class TempSensor : public Sensor {
public:
  TempSensor(int sensorId, String name, unsigned long pollInterval);
  int getSensorValue();
  int getSensorState();
  void tempThresholdTripped();
  int getHrTemp();
  int getTemp(byte reg);
  void startConversion(boolean start);
  void setThresh(byte reg, int tC);
  void setConfig(byte cfg);
private:
 
};
#endif
