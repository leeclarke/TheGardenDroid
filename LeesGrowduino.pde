#include <Wire.h>
#include <EEPROM.h>
#include "RtcSensor.h"

//IC2 notes 
// SDA pin is Analog4
// SCL pin is Analog5
// DS1621 has A2, A1, and A0 pins connected to GND
// device ID and address
#define DEV_TYPE   0x90 >> 1                    // shift required by wire.h
#define DEV_ADDR   0x00                         // DS1621 address is 0
#define SLAVE_ID   DEV_TYPE | DEV_ADDR
#define RTC_ID     0x68                         //Set like example
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
//Setting Constants to remind me which interupt is on which pin.. aka: self-doucmenting code ;)
const int PIN2_INTERRUPT = 0;
const int PIN3_INTERRUPT = 1;
const int ledPin =  13;      // the number of the LED pin
const int relayPin =  12;      // the number of the Relay which drives the Grow Light
int ledState = LOW;             // ledState used to set the LED
int value;

RtcSensor rtc(0,"RTC", 1000);

//TODO: Convert Temp Sensor to an object.
void setup()
{  
  //Config Interrupt to notify if temp threshold is tripped
  pinMode(PIN2, INPUT);
  digitalWrite(PIN2, HIGH);
  attachInterrupt(PIN2_INTERRUPT, tempThresholdTripped, CHANGE);
  Wire.begin();                                 // connect I2C
  startConversion(false);                       // stop if presently set to continuous
  setConfig(POL | ONE_SHOT);                    // Tout = active high; 1-shot mode
  setThresh(ACCESS_TH, 23);                     // high temp threshold = 80F
  setThresh(ACCESS_TL, 20);                     // low temp threshold = 75F
  Serial.begin(9600);
  delay(5);
  Serial.println("DS1621 Test");
  int tHthresh = getTemp(ACCESS_TH);
  Serial.print("High threshold = ");
  Serial.println(tHthresh);
  int tLthresh = getTemp(ACCESS_TL);
  Serial.print("Low threshold = ");
  Serial.println(tLthresh);
  
  //TODO: Add Flash management or config.
  //Test writing to Flash
  value = EEPROM.read(0);
  Serial.print(0);
  Serial.print("\t");
  Serial.print(value);
  Serial.println();
  EEPROM.write(0,69);
}

//TODO: Add Metro for polling Sensors, consider puttign Sensors into a map/array
void loop()
{
  Serial.println("In Loop");
  // if the LED is off turn it on and vice-versa:
  if (ledState == LOW)
    ledState = HIGH;
  else
    ledState = LOW;
  // set the LED with the ledState of the variable:
  digitalWrite(ledPin, ledState);
  int tC, tFrac;
  tC = getHrTemp();                             // read high-resolution temperature
  if (tC < 0) {
    tC = -tC;                                   // fix for integer division if negitive
    Serial.print("-");                          // indicate negative
  }
  tFrac = tC % 100;                             // extract fractional part
  tC /= 100;                                    // extract whole part
  double tmpC = tC + (tFrac*.01);
  double tempF = (tmpC*9/5)+32;
  Serial.print(tmpC);
  Serial.print(" C / ");
  Serial.print(tempF);
  Serial.println(" F");
  if(digitalRead(PIN2))
    Serial.println("** PIN2 == true **");
  else
    Serial.println("** PIN@ == false **");
    
  delay(1000);
  
  //Check the RTC
  //byte second, minute, hour, dayOfWeek, dayOfMonth, month, year;
 //TODO:  getDateDs1307(&second, &minute, &hour, &dayOfWeek, &dayOfMonth, &month, &year);
  Serial.println(rtc.getTimestamp()); 
 
//  Serial.print(hour, DEC);
//  Serial.print(":");
//  Serial.print(minute, DEC);
//  Serial.print(":");
//  Serial.print(second, DEC);
//  Serial.print("  ");
//  Serial.print(month, DEC);
//  Serial.print("/");
//  Serial.print(dayOfMonth, DEC);
//  Serial.print("/");
//  Serial.print(year, DEC);
//  Serial.print("  Day_of_week:");
//  Serial.println(dayOfWeek, DEC); 
  delay(2000); //wait 2 sec
  
  
  digitalWrite(relayPin, HIGH);
  delay(2000); //Turn on the Grow Light for 2 sec
  digitalWrite(relayPin, LOW);
 }
// Set configuration register
void setConfig(byte cfg)
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
// Sets temperature threshold
// -- whole degrees C only
// -- works only with ACCESS_TL and ACCESS_TH
void setThresh(byte reg, int tC)
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
// Start/Stop DS1621 temperature conversion
void startConversion(boolean start)
{
  Wire.beginTransmission(SLAVE_ID);
  if (start == true)
    Wire.send(START_CNV);
  else
    Wire.send(STOP_CNV);
  Wire.endTransmission();
}
// Reads temperature or threshold
// -- whole degrees C only
// -- works only with RD_TEMP, ACCESS_TL, and ACCESS_TH
int getTemp(byte reg)
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
int getHrTemp()
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
void tempThresholdTripped()
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
