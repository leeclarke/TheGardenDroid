/*
  GrowLite.cpp - Moisture sensor interface specifically for the Vegetronix attached to the Arduino.
  Note that Poll time is in 
  
  Created by Lee Clarke, Nov 05, 2010.
  Released into the public domain.
*/

#include <Wire.h>
#include "Sensor.h"
#include "GrowLite.h"
#include "PString.h"

GrowLite::GrowLite(int sensorId, String name, int pinLocation){

    this->pin = pinLocation; //Digital Pin for this.
}

int GrowLite::getStatus() {
  return this->status;
}

String GrowLite::getStatusDesc() {
    if(this->status == 0)
      return "Off";
    return "On";
}
  
void GrowLite::setDigitalPin(int pin) {
    this->pin = pin;
}
  
int GrowLite::getDigitalPin() {
    return this->pin;
}
  
void GrowLite::setStartTime(int hour, int minute) {
  this->startHour = hour;
  this->startMin  = minute;
}
  
void GrowLite::setEndTime(int hour, int minute) {
  this->endHour = hour;
  this->endMin  = minute;
}

/**
 * Returns a String formatted representation of the Starting time or :: if no value was set.
 */
String GrowLite::getStartTimeStr(){
  return "";
}

/**
 * Returns a String formatted representation of the End time or :: if no value was set.
 */
String GrowLite::getEndTimeStr() {
  return "";
}

/**
 * Determines if its time to toggle the light and di it if needed. Returns indicator that it started or stopped if interested. 
 * @return [-1, no change 0 off, 1 on]
 */
int checkTime(int hour, int minute){
  
  return -1;
}

