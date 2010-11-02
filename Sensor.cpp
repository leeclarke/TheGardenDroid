/*
  Sensor.cpp - Library for modeling Sensors attached to the Arduino.
  When impleemnting, override getSensorValue() and getSensorState()
  Created by Lee Clarke, Oct 20, 2010.
  Released into the public domain.
*/
#include "WProgram.h"
#include "Sensor.h"

Sensor::Sensor(int sensorId, String name, unsigned long pollInterval){
  _id = sensorId;
  _name = name;
  _pollInterval = pollInterval;
}

void Sensor::setLowThreshold(int value){
  _lowThresh = value;

}

void Sensor::setHighThreshold(int value){
  _highThresh = value;
}

void Sensor::setPollInterval(unsigned long mills){
  _pollInterval  = mills;
}

unsigned long Sensor::getPollInterval(){
  return _pollInterval;
}
 
int Sensor::getSensorValue(){
  return -1;
}

int Sensor::getSensorState(){
  return 0;
}

int Sensor::getSensorID(){
  return _id;
}

String Sensor::getSensorName(){
  return _name;
} 

char Sensor::check()
{
  Serial.print("## NAME=");
  Serial.print(this->getSensorName());
  Serial.print(" Mills=");
  Serial.print(millis());
  Serial.print(" previous_millis=");
  Serial.print(this->previous_millis);
  Serial.print(" _pollInterval=");
  Serial.println(this->_pollInterval);
  
  if (millis() - this->previous_millis >= this->_pollInterval) {
   
    if (this->_pollInterval == 0 || this->autoreset ) {
      this->previous_millis = millis();
    } 
    else {
      this->previous_millis += this->_pollInterval; 
    }

    return 1;
  }
  return 0;
}

void Sensor::reset() 
{
  this->previous_millis = millis();
}
