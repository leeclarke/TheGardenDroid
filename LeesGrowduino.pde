#include <Wire.h>
#include <EEPROM.h>
#include "RtcSensor.h"
#include "TempSensor.h"
//TODO: Test Sensor functionality.

//IC2 notes 
// SDA pin is Analog4
// SCL pin is Analog5
// DS1621 has A2, A1, and A0 pins connected to GND
// device ID and address

//#define RTC_ID     0x68                         //Set like example
// DS1621 Registers & Commands
//#define RD_TEMP    0xAA                         // read temperature register
#define ACCESS_TH  0xA1                         // access high temperature register
#define ACCESS_TL  0xA2                         // access low temperature register
//#define ACCESS_CFG 0xAC                         // access configuration register
/*#define RD_CNTR    0xA8                         // read counter register
#define RD_SLOPE   0xA9                         // read slope register
#define START_CNV  0xEE                         // start temperature conversion
#define STOP_CNV   0X22                         // stop temperature conversion

// DS1621 configuration bits
#define DONE       B10000000                    // conversion is done
#define THF        B01000000                    // high temp flag
#define TLF        B00100000                    // low temp flag
#define NVB        B00010000                    // non-volatile memory is busy
*/
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
TempSensor temp(0,"TEMP", 1000);


void setup()
{  
  //Config Interrupt to notify if temp threshold is tripped
  pinMode(PIN2, INPUT);
  digitalWrite(PIN2, HIGH);
  attachInterrupt(PIN2_INTERRUPT, tempThresholdTripped, CHANGE);
  Wire.begin();                                 // connect I2C
  byte tempStart = temp.startConversion(false);      // start/stop returns code indicating successful contact with sensor.
  if(tempStart >0) {
     Serial.print("TempSensor is not responding code:");
     Serial.println(tempStart);
  }
  
  temp.setConfig(POL | ONE_SHOT);                    // Tout = active high; 1-shot mode
  temp.setThresh(ACCESS_TH, 23);                     // high temp threshold = 80F
  temp.setThresh(ACCESS_TL, 20);                     // low temp threshold = 75F
  
  Serial.begin(9600);
  delay(5);
  Serial.println("DS1621 Test");
  int tHthresh = temp.getTemp(ACCESS_TH);
  
  Serial.print("High threshold = ");
  Serial.println(tHthresh);
  int tLthresh = temp.getTemp(ACCESS_TL);
  Serial.print("Low threshold = ");
  Serial.println(tLthresh);
  
  //TODO: Add Flash management or config.
  //Test writing to Flash
  value = EEPROM.read(0);
  Serial.print("EEPROM TEst ");
  Serial.print(0);
  Serial.print("\t");
  Serial.print(value);
  Serial.println();
  EEPROM.write(0,69);
}

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
/*  int tC, tFrac;
  tC = temp.getHrTemp();                             // read high-resolution temperature
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
  */
  Serial.println( temp.toString());
 // if(digitalRead(PIN2))
 //   Serial.println("** PIN2 == true **");
 // else
 //   Serial.println("** PIN@ == false **");
    
  delay(1000);
  
  //Check the RTC
 rtc.getSensorValue();
 
  //Serial.println(rtc.getTimestamp()); 
//TODO: Figure out how to convert to ASCII so getTimestamp() will work!! 
  printTimestamp();
  delay(2000); //wait 2 sec
  
  
  digitalWrite(relayPin, HIGH);
  delay(2000); //Turn on the Grow Light for 2 sec
  digitalWrite(relayPin, LOW);
 }

void printTimestamp()
{
 Serial.print(rtc.hour, DEC);
  Serial.print(":");
  Serial.print(rtc.minute, DEC);
  Serial.print(":");
  Serial.print(rtc.second, DEC);
  Serial.print("  ");
  Serial.print(rtc.month, DEC);
  Serial.print("/");
  Serial.print(rtc.dayOfMonth, DEC);
  Serial.print("/");
  Serial.print(rtc.year, DEC);
  Serial.print("  Day_of_week:");
  Serial.println(rtc.dayOfWeek, DEC); 
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
