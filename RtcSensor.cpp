/*
  RtcSensor.cpp - Real Time Clock sensor interface specificly for the DS1307 attached to the Arduino.
  Created by Lee Clarke, Oct 21, 2010.
  Released into the public domain.
*/

#include <Wire.h>
#include "Sensor.h"
#include "RtcSensor.h"
#include "PString.h"

#define RTC_ID     0x68

RtcSensor::RtcSensor(int sensorId, String name, unsigned long pollInterval)
  :Sensor(sensorId, name, pollInterval){
}

int RtcSensor::getSensorState(){
  int resp = 0;
  if (this->sendStatus > 0)
  {
    resp = (int)this->sendStatus;    
  }
  else {
    //TODO: decide if want to add additional codes or return 0
  }
  return resp; 
}

/**
 * Returns a human readable timestamp in format yyyy-MM-dd HH:mm:ss
 --Doesnt work, the chars need to be converted to ASCII
 */
String RtcSensor::getTimestamp(){
  char buf[30];
  PString str(buf, sizeof(buf));
  String resp;
  
  str.format("20%d-%d-%d %d:%d:%d",year, month, dayOfMonth, hour, minute, second);

  resp = String(buf);
  return resp;
}

//To call inherited methods  Sensor::getPollInterval()

/**
 * Retrieves the Time values and sets to public vars but only if it is time to execute a polling request.
 * 
 * Return 1 for success, 0 if data was not due for update, -1 for fail.
 */
int RtcSensor::getSensorValue() {
  int resp = 1;
    // Reset the register pointer
    Wire.beginTransmission(RTC_ID);
    Wire.send(0);
    this->sendStatus = Wire.endTransmission();
    Wire.requestFrom(RTC_ID, 7);
    // A few of these need masks because certain bits are control bits
    this->second     = bcdToDec(Wire.receive() & 0x7f);
    this->minute     = bcdToDec(Wire.receive());    
    this->hour       = bcdToDec(Wire.receive() & 0x3f);  // Need to change this if 12 hour am/pm
    this->dayOfWeek  = bcdToDec(Wire.receive());
    this->dayOfMonth = bcdToDec(Wire.receive());
    this->month      = bcdToDec(Wire.receive());
    this->year = bcdToDec(Wire.receive());
    // Error check, if all values or at least the date bytes are ==0 then read failed.
    if(year == 0 || month == 0 || dayOfMonth == 0)
    {
      resp = -1;
      this->sendStatus = -1;
    }

  return resp;
}

// Convert normal decimal numbers to binary coded decimal
byte RtcSensor::decToBcd(byte val)
{
  return ( (val/10*16) + (val%10) );
}
// Convert binary coded decimal to normal decimal numbers
byte RtcSensor::bcdToDec(byte val)
{
  return  ( (val/16*10) + (val%16) );
}

/**
 * Sets the date/time on the ds1307,  starts the Clock and defaults to 24Hr clock
 * Assumes you're passing in valid numbers
 *    EX.  second = 45;   minute = 17;   hour = 23;   dayOfWeek = 7;   dayOfMonth = 17;   month = 10;   year = 10; (45,17,22,1,8,11,10)
 *         setDateDs1307(second, minute, hour, dayOfWeek, dayOfMonth, month, year);
 */
void RtcSensor::setDateDs1307(byte second,        // 0-59
                   byte minute,        // 0-59
                   byte hour,          // 1-23
                   byte dayOfWeek,     // 1-7
                   byte dayOfMonth,    // 1-28/29/30/31
                   byte month,         // 1-12
                   byte year)          // 0-99
{
   Wire.beginTransmission(RTC_ID);
   Wire.send(0);
   Wire.send(decToBcd(second));    // 0 to bit 7 starts the clock
   Wire.send(decToBcd(minute));
   Wire.send(decToBcd(hour));      // If you want 12 hour am/pm you need to set
                                   // bit 6 (also need to change readDateDs1307)
   Wire.send(decToBcd(dayOfWeek));
   Wire.send(decToBcd(dayOfMonth));
   Wire.send(decToBcd(month));
   Wire.send(decToBcd(year));
   Wire.endTransmission();
} 


