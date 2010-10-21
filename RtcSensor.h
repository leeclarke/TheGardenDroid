/*
  RtcSensor.h - Real Time Clock sensor interface specificly for the DS1307 attached to the Arduino.
  Created by Lee Clarke, Oct 21, 2010.
  Released into the public domain.
*/
#ifndef RtcSensor_h
#define RtcSensor_h

#include "PollEvent.h"
#include "Sensor.h"

class RtcSensor : public Sensor {
public:
  RtcSensor(int sensorId, String name, unsigned long pollInterval);
  void setDateDs1307(byte second, byte minute, byte hour, byte dayOfWeek, byte dayOfMonth, byte month, byte year);
  String getTimestamp();
  int getSensorValue();
  int getSensorState();
  byte second;
  byte minute;
  byte hour;
  byte dayOfWeek;
  byte dayOfMonth;
  byte month;
  byte year;
private:
  byte decToBcd(byte val);
  byte bcdToDec(byte val);
 
};
#endif
