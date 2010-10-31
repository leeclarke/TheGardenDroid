/*
  MoistureSensor.h - Moisture sensor interface specifically for the Vegetronix attached to the Arduino.
  Created by Lee Clarke, Oct 30, 2010.
  Released into the public domain.
*/
#ifndef MoistureSensor_h
#define MoistureSensor_h

#include "PollEvent.h"
#include "Sensor.h"

class MoistureSensor : public Sensor {
public:
  MoistureSensor(int sensorId, String name, unsigned long pollInterval, int pinLocation);
  int getSensorValue();
  String getSensorValueDesc();
  int getSensorState();
  void setAnaloguePin(int pin);
  int getAnaloguePin();
private:
  int pin;
  int sensorValue;
};
#endif
