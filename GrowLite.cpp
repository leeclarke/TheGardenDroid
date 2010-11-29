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
 * Determines if its time to toggle the light. Returns indicator that it started or stopped if interested. 
 * @return [-1, no change 0 off, 1 on]
 */
int GrowLite::checkTime(int hour, int minute){
  int change = -1;
  Serial.print("sHour=");
  Serial.print(this->startHour);
  Serial.print(" sMin=");
  Serial.print(this->startMin);
  Serial.print(" eHour=");
  Serial.print(this->endHour);
  Serial.print(" eMin=");
  Serial.print(this->endMin);
  
  //TODO Fix, need to compare hours then mins
  if(hour >= this->startHour && minute >= this->startMin && hour <= this->endHour && minute <= this->endMin)
  {
    if(this->status != 1) {
      digitalWrite( this->pin,HIGH);
      this->status = 1;
      change = 1;
    }
  }
  else
  {
    if(this->status != 0) {
      digitalWrite( this->pin,LOW);
      this->status = 0;
      change = 0;
    }
  }
  
  return change;
}

