/**
 * TempSensor.cpp -Temprature sensor interface specificly for the DS1621 attached to the Arduino.
 * Created by Lee Clarke, Oct 21, 2010.
 * Released into the public domain.
 */
#include "TempSensor.h"
#include "PString.h"
#include "Sensor.h"

#include <Wire.h> 

#define DEV_TYPE   0x90 >> 1                    // shift required by wire.h
#define DEV_ADDR   0x00                         // DS1621 address is 0
#define SLAVE_ID   DEV_TYPE | DEV_ADDR

// DS1621 Registers & Commands
#define RD_TEMP    0xAA                         // read temperature register
#define ACCESS_TH  0xA1                         // access high temperature register
#define ACCESS_TL  0xA2                         // access low temperature register
#define ACCESS_CFG 0xAC                         // access configuration register
#define RD_CNTR    0xA8                         // read counter register
#define RD_SLOPE   0xA9                         // read slope register
#define START_CNV  0xEE                         // start temperature conversion
#define STOP_CNV   0X22                         // stop temperature conversion

// DS1621 configuration bits
#define DONE       B10000000                    // conversion is done
#define THF        B01000000                    // high temp flag
#define TLF        B00100000                    // low temp flag
#define NVB        B00010000                    // non-volatile memory is busy
#define POL        B00000010                    // output polarity (1 = high, 0 = low)
#define ONE_SHOT   B00000001                    // 1 = one conversion; 0 = continuous conversion

TempSensor::TempSensor(int sensorId, String name, unsigned long pollInterval)                                                                                                      
  :Sensor(sensorId, name, pollInterval){
     negTempC = false;
  }

/**
 * Retrieves the Temp values and sets results to fields.
 * 
 * Return 1 for success, 0 if data was not due for update, -1 for fail.
 */
int TempSensor::getSensorValue() {
  int resp = -1;
  int tC, tFrac;
  tC = getHrTemp();                             // read high-resolution temperature
  if (tC < 0) {
    tC = -tC;                                   // fix for integer division if negitive
    //resp += "-";                          // indicate negative
    negTempC = true;
  }
  tFrac = tC % 100;                             // extract fractional part
  tC /= 100;                                    // extract whole part
  tempC = tC + (tFrac*.01);
  tempF = (tempC*9/5)+32;
  resp = 1;
  return resp;
}

int TempSensor::getSensorState(){
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


/* --------Temp support-----------*/

// Start/Stop DS1621 temperature conversion
void TempSensor::startConversion(boolean start)
{
  Wire.beginTransmission(SLAVE_ID);
  if (start == true)
    Wire.send(START_CNV);
  else
    Wire.send(STOP_CNV);
  this->sendStatus = Wire.endTransmission();
}

// Set configuration register
void TempSensor::setConfig(byte cfg)
{
  Wire.beginTransmission(SLAVE_ID);
  Wire.send(ACCESS_CFG);
  Wire.send(cfg);
  Wire.endTransmission();
  delay(15);                                    // allow EE write time to finish
}

// Read a DS1621 register
byte getReg(byte reg)
{
  Wire.beginTransmission(SLAVE_ID);
  Wire.send(reg);                               // set register to read
  Wire.endTransmission();
  Wire.requestFrom(SLAVE_ID, 1);
  byte regVal = Wire.receive();
  return regVal;
} 

void TempSensor::setHighThresh(int tC) {
  setThresh(ACCESS_TH,tC);
}

void TempSensor::setLowThresh(int tC) {
  setThresh(ACCESS_TL,tC);
}

// Sets temperature threshold
// -- whole degrees C only
// -- works only with ACCESS_TL and ACCESS_TH
void TempSensor::setThresh(byte reg, int tC)
{
  if (reg == ACCESS_TL || reg == ACCESS_TH) {
    Wire.beginTransmission(SLAVE_ID);
    Wire.send(reg);                             // select temperature reg
    Wire.send(byte(tC));                        // set threshold
    Wire.send(0);                               // clear fractional bit
    Wire.endTransmission();
    delay(15);
  }
}

// Reads temperature or threshold
// -- whole degrees C only
// -- works only with RD_TEMP, ACCESS_TL, and ACCESS_TH
int TempSensor::getTemp(byte reg)
{
  int tC;
  if (reg == RD_TEMP || reg == ACCESS_TL || reg == ACCESS_TH) {
    byte tVal = getReg(reg);
    if (tVal >= B10000000) {                    // negative?
      tC = 0xFF00 | tVal;                       // extend sign bits
    }
    else {
      tC = tVal;
    }
    return tC;                                  // return threshold
  }
  return 0;                                     // bad reg, return 0
}

// Read high resolution temperature
// -- returns temperature in 1/100ths degrees
// -- DS1620 must be in 1-shot mode
int TempSensor::getHrTemp()
{
  startConversion(true);                        // initiate conversion
  byte cfg = 0;
  while (cfg < DONE) {                          // let it finish
    cfg = getReg(ACCESS_CFG);
  }
  int tHR = getTemp(RD_TEMP);                   // get whole degrees reading
  byte cRem = getReg(RD_CNTR);                  // get counts remaining
  byte slope = getReg(RD_SLOPE);                // get counts per degree
  if (tHR >= 0)
    tHR = (tHR * 100 - 25) + ((slope - cRem) * 100 / slope);
  else {
    tHR = -tHR;
    tHR = (25 - tHR * 100) + ((slope - cRem) * 100 / slope);
  }
  return tHR;
}

void TempSensor::tempThresholdTripped()
{
  Serial.print("###  Temp Thresholds Exceeded!   ####");
  Serial.print("** PIN2 == true **");
  /* I plan to use this as a freeze warning indicator to trigger heat, I wonder if it would work
   * in reverse.. say setting high to 22 and Low to say 24, would that cause it to send tOut high
   * when it hits 22 and set Low ehn it goes back up to 24?*/
  /* NOTE: if the temp is between threshH and threshL   when system starts then tOut ==0 
   * This means that the los threshold will NOT trigger an alarm. 
   * I think the solution would be to set the ThreshH to currentTemp-1 or some lower 
   * alarm value such as 0C this would enable a trigger when the temp drops low like a 
   * freeze alarm.
   */
  Serial.println("");
}

String TempSensor::toString()
{ 
  this->getSensorValue();
  char buf[20];
  PString str(buf, sizeof(buf));
  String resp;
  
  if(negTempC){
    str.print("-");
  }
  str.print(tempC);
  str.print("|");
  str.print(tempF);
//  str.print("F");

  resp = String(buf);

return resp;
}
