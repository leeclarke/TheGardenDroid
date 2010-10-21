/*
  TempSensor.h -Temprature sensor interface specificly for the DS1621 attached to the Arduino.
  Created by Lee Clarke, Oct 21, 2010.
  Released into the public domain.
*/
#ifndef TempSensor_h
#define TempSensor_h

#include "PollEvent.h"
#include "Sensor.h"

class TempSensor : public Sensor {
public:
  TempSensor(int sensorId, String name, unsigned long pollInterval);
  int getSensorValue();
  int getSensorState();

private:
 
};
#endif
