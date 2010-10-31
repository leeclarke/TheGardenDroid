/*
  RtcSensor.cpp - Moisture sensor interface specifically for the Vegetronix attached to the Arduino.
  Created by Lee Clarke, Oct 30, 2010.
  Released into the public domain.
*/

#include <Wire.h>
#include "PollEvent.h"
#include "Sensor.h"
#include "MoistureSensor.h"
#include "PString.h"

MoistureSensor::MoistureSensor(int sensorId, String name, unsigned long pollInterval, int pinLocation)
  :Sensor(sensorId, name, pollInterval){
    this->pin = pinLocation;
}

/**
 * Returns a -1 if there is no reading, 1 if all is good.
 */
int MoistureSensor::getSensorState(){
  int resp = 1;
  if (this->sensorValue <= 0)
  {
    resp = -1;    
  }
  return resp; 
}

/** 
 * Translates the sensor value into english.
 */
String MoistureSensor::getSensorValueDesc() {
  return "Finish me";
}

/**
 * Returns the raw analog reading
 */
int MoistureSensor::getSensorValue() {
  this->sensorValue = analogRead(pin);
  return this->sensorValue;
}

void MoistureSensor::setAnaloguePin(int pin){
  this->pin = pin;
}

int MoistureSensor::getAnaloguePin() {
  return this->pin;
}
