#include <Wire.h>
#include <EEPROM.h>
#include "RtcSensor.h"
#include "TempSensor.h"
#include "MoistureSensor.h"
#include "PString.h"

// DS1621 configuration bits for ooutputting results
#define POL        B00000010                    // output polarity (1 = high, 0 = low)
#define ONE_SHOT   B00000001                    // 1 = one conversion; 0 = continuous conversion


//Setting Constants to remind me which interupt is on which pin.. aka: self-doucmenting code ;)
const int PIN2_INTERRUPT = 0;
const int PIN3_INTERRUPT = 1;
const int LED_D_PIN =  13;           // the number of the LED pin
const int LITE_RELAY_D_PIN =  12;     // the number of the Relay which drives the Grow Light
const int I2C_SDA_DPIN = 4;          //Managed by the Sensor object but noted here for ref.
const int I2C_SCL_DPIN = 5;
const int VEGI_A_PIN = 0;            //Analog read for Vegitronix


int ledState = LOW;             // ledState used to set the LED
int value;
byte tempStart = 0;

RtcSensor rtc(0,"RTC", 1000);
TempSensor temp(0,"TEMP", 1000);
MoistureSensor moist(0,"MOIST", 10000, VEGI_A_PIN);


void setup()
{  
  //rtc.setDateDs1307(45,17,22,1,8,11,10);
  //Config Interrupt to notify if temp threshold is tripped
  //TODO: Update with actual hardware pin #
  pinMode(PIN2, INPUT);
  digitalWrite(PIN2, HIGH);
  attachInterrupt(PIN2_INTERRUPT, tempThresholdTripped, CHANGE);
  Wire.begin();                                 // connect I2C

  Serial.begin(9600);
  delay(5);
  Serial.println("Growduino Test");

  temp.startConversion(false);      // start/stop returns code indicating successful contact with sensor.
  if(temp.getSensorState() > 0) {

    Serial.print("TempSensor is not responding code:");
    Serial.println( temp.getSensorState());
  }
  else {
    Serial.print("TempSensor OK:");
    Serial.println(temp.sendStatus);
    temp.setConfig(POL | ONE_SHOT);                    // Tout = active high; 1-shot mode
    temp.setHighThresh(23);                     // high temp threshold = 80F
    temp.setLowThresh(20);                     // low temp threshold = 75F
    //    int tHthresh = temp.getTemp(ACCESS_TH);
    //    Serial.print("High threshold = ");
    //    Serial.println(tHthresh);
    //    int tLthresh = temp.getTemp(ACCESS_TL);
    //    Serial.print("Low threshold = ");
    //    Serial.println(tLthresh);
  }

  //TODO: Add Flash management or config.
  //Test writing to Flash
  value = EEPROM.read(0);
  Serial.print("EEPROM TEst1 ");
  Serial.print(0);
  Serial.print("\t");
  Serial.print(value);
  Serial.println();
  EEPROM.write(0,69);
}
/*
void checkForCommand() {  
 if (Serial.available() > 0) {
 // get incoming byte:
 inByte = Serial.read();
 // read first analog input, divide by 4 to make the range 0-255:
 firstSensor = analogRead(A0)/4;
 // delay 10ms to let the ADC recover:
 delay(10);
 } 
 }*/

void loop()
{
  Serial.println("In Loop");
  //checkForCommand();
  // if the LED is off turn it on and vice-versa:
  if (ledState == LOW)
    ledState = HIGH;
  else
    ledState = LOW;
  // set the LED with the ledState of the variable:
  digitalWrite(LED_D_PIN, ledState);

  //Output temp
  if(temp.getSensorState() == 0) {
    if(temp.check() == 1)
      Serial.println( temp.toString());
  }
  else {
    Serial.println("Temprature Sensor is returning an error.");
  }
  // if(digitalRead(PIN2))
  //   Serial.println("** PIN2 == true **");
  // else
  //   Serial.println("** PIN@ == false **");

  //  delay(1000);

  //Check the RTC
  if(rtc.check() == 1)
  {
    int resp = rtc.getSensorValue();
    Serial.print("read Resp=");
    Serial.println(resp);
    if(rtc.getSensorState()>0) {
      Serial.print("RTC Error= ");
      Serial.println(rtc.getSensorState());
    }
    else{
      Serial.println(rtc.getTimestamp()); 
    }
  }

  //read moisture sensor
  if(moist.check() == 1)
  {
    Serial.print("MoistureVal= ");
    Serial.println(moist.getSensorValue());
  }

  //delay(2000); //wait 2 sec
  digitalWrite(LITE_RELAY_D_PIN, HIGH);
  delay(2000); //Turn on the Grow Light for 2 sec
  digitalWrite(LITE_RELAY_D_PIN, LOW);

  //EEPROM Test
  value = EEPROM.read(0);
  Serial.print("EEPROM TEst2 ");
  Serial.print(0);
  Serial.print("\t");
  Serial.print(value);
  Serial.println();
}

//Send alert through RF connection and light up Red led on pin 12
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
}


