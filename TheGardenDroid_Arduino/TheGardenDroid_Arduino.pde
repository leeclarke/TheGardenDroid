
#include <Messenger.h>
#include <Wire.h>
#include <EEPROM.h>
#include "RtcSensor.h"
#include "TempSensor.h"
#include "MoistureSensor.h"
#include "GrowLite.h"

/* DS1621 configuration bits for ooutputting results*/
#define POL        B00000010                    // output polarity (1 = high, 0 = low)
#define ONE_SHOT   B00000001                    // 1 = one conversion; 0 = continuous conversion


//Setting Constants to remind me which interupt is on which pin.. aka: self-doucmenting code ;)
const int PIN2_INTERRUPT = 0;
const int PIN3_INTERRUPT = 1;
const int LED_D_PIN =  13;           // the number of the LED pin
const int LED_DEBUG2 =  10;           // the number of the LED pin
const int LITE_RELAY_D_PIN =  12;     // the number of the Relay which drives the Grow Light
const int I2C_SDA_DPIN = 4;          //Managed by the Sensor object but noted here for ref.
const int I2C_SCL_DPIN = 5;
const int VEGI_A_PIN = 0;            //Analog read for Vegitronix

const int TEMP_DELAY = 10000;
const int MOIST_DELAY = 20500;
const int HUMIDITY_DELAY = 40500;

char MSG_ERR   = 'E';
char MSG_LOG   = 'L';
char MSG_TEMP  = 'T';
char MSG_TIME  = 'I';
char MSG_MOIST = 'M';
char MSG_LITE  = 'G';
char MSG_TEMP_WARN  = 'W';

int ledState = LOW;             // ledState used to set the LED
int led2State = LOW;             // ledState used to set the LED
int value;
byte tempStart = 0;
int rfMessageId = 0;

RtcSensor rtc(0,"RTC", 500);
TempSensor temp(0,"TEMP", TEMP_DELAY);
MoistureSensor moist(0,"MOIST", MOIST_DELAY, VEGI_A_PIN);
GrowLite glite(1,"GROW", LITE_RELAY_D_PIN);
Messenger message = Messenger(); 

void setup()
{  
   pinMode(LITE_RELAY_D_PIN, OUTPUT);
   digitalWrite(LITE_RELAY_D_PIN, HIGH);
   delay(2000);
   digitalWrite(LITE_RELAY_D_PIN, LOW);
  //Config Interrupt to notify if temp threshold is tripped
  //TODO: Update with actual hardware pin #
 // pinMode(PIN2, INPUT);
 // digitalWrite(PIN2, HIGH);
 // attachInterrupt(PIN2_INTERRUPT, tempThresholdTripped, CHANGE);
  Wire.begin();                                 // connect I2C
  //rtc.setDateDs1307(45,32,23,7,28,11,10);

  Serial.begin(1200); //No rush and better distance.
  message.attach(messageReady);
  
  delay(600); //waits for the RTC to update its time settings
  
  transmitData(MSG_LOG, "[Starting GardenDroid]");
  
  temp.startConversion(false);      // start/stop returns code indicating successful contact with sensor.
  if(temp.getSensorState() > 0) {
    String msg = String("TempSensor is not responding code:");
    msg.concat(temp.getSensorState());
    transmitData(MSG_ERR, msg);
  }
  else {
    String msg = String("LOGTempSensor OK");
    msg.concat(temp.getSensorState());
    transmitData(MSG_LOG, msg);
    
    temp.setConfig(POL | ONE_SHOT);                    // Tout = active high; 1-shot mode
    //temp.setHighThresh(23);                     // high temp threshold = 80F
    //temp.setLowThresh(20);                     // low temp threshold = 75F
  }
  glite.setStartTime(19,56);
  glite.setEndTime(7,56);//Set for 12 hrs
}

//###########
void loop()
{
  while ( Serial.available() ) {
    message.process(Serial.read () );
  }
  toggleDebugLED();

  //  #### Check the RTC  ####
  if(rtc.check() == 1)
  {
      int resp = rtc.getSensorValue();

      if(rtc.getSensorState()>0) {
        String msg = String("RTC:");
        msg.concat(rtc.getSensorState());
        transmitData(MSG_ERR, msg);
      }
      else{
        rtc.getTimestamp();
        
        //  #### Check Grow lite schedule #### 
        //The activity is time based so no point firing if RTC hasnt updated.
        if(glite.check() == 1)
        {
          int gstatus = glite.checkTime((int)rtc.hour, (int)rtc.minute);
          if(gstatus > -1) {
            transmitData(MSG_LITE, gstatus);
          }
        }
      }
  }

  // #### Read temp ####
  if(temp.getSensorState() == 0) {
    if(temp.check() == 1) {
      transmitData(MSG_TEMP, temp.toString());
    }
  }
  else {
    transmitData(MSG_ERR, "Temprature Sensor is returning an error.");
  }

  //   #### read moisture sensor ####
  if(moist.check() == 1)
  {
    transmitData(MSG_MOIST, moist.getSensorValue());
  }
   
  blinkDebugLED();   
}



void blinkDebugLED() {
  toggleDebugLED();
  delay(2000);
  toggleDebugLED();
}

void toggleDebugLED() {
  if (ledState == LOW)
    ledState = HIGH;
  else
    ledState = LOW;
  // set the LED with the ledState of the variable:
  digitalWrite(LED_D_PIN, ledState);
}

void blinkDebug2LED() {
  digitalWrite(LED_DEBUG2, HIGH);
  delay(2000);
  digitalWrite(LED_DEBUG2, LOW);
}

String getRFMessageID()
{
  int tempId = rfMessageId;
  if(rfMessageId >99) {
     rfMessageId = 0; 
  }
  if(tempId >99) {
     return  getRFMessageID(); //if the number is wrong try again
  }   
  
  String rfId;
  if(tempId >10)  {
    rfId = String(tempId);
  }
  else {
       rfId = String("0"); 
       rfId.concat(tempId);
  }
  return rfId;
}

/** 
 * Sends a String with the data message wrapper including a timestamp
 */
void transmitData(char msgType, String data)
{  
    char buf[data.length()];
    data.toCharArray(buf, data.length());
  
    transmitData(msgType,buf);
}

/** 
 * Sends a char string with the data message wrapper including a timestamp
 */
void transmitData(char msgType, char data[])
{ 
    Serial.write(0x01);
    String id = getRFMessageID();
    char ids[id.length()];
    id.toCharArray(ids,id.length());
    
    Serial.write(ids);
    Serial.write(0x02);
    Serial.write(msgType);

    String ts = getTimestamp();
    char buf[ts.length()];
    ts.toCharArray(buf, ts.length());
    Serial.write(buf);
    Serial.write('|');
    Serial.write(data);
    Serial.write(0x04);
    Serial.write(0x04);
    delay(500);  //Give the computer a half sec to process the message
}

/** 
 * Sends an integer value with the data message wrapper including a timestamp
 */
void transmitData(char msgType,int data)
{
    Serial.write(0x01);
    String id = getRFMessageID();
    char ids[id.length()];
    id.toCharArray(ids,id.length());
    
    Serial.write(ids);
    Serial.write(0x02);
    Serial.write(msgType);

    String ts = getTimestamp();
    char buf[ts.length()];
    ts.toCharArray(buf, ts.length());
    Serial.write(buf);
    
    Serial.write('|');
    Serial.print(data);
    Serial.write(0x04);
    Serial.write(0x04);
    delay(500);  //Give the computer a half sec to process the message
}

/**
 * Wrapper ensures that the RTC is functioning before trying to call the getTimestamp Method.
 */
String getTimestamp() 
{
  String resp = String();
  if(rtc.getSensorState()>0) {
    resp = String(rtc.getSensorState());
  }
  else{
    resp  = rtc.getTimestamp(); 
  }
  return resp;
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
      
// Create the callback function
void messageReady() {
       // Loop through all the available elements of the message
       blinkDebug2LED();
       if(message.available())
       {
          //check for command char
          if(message.checkString("T")) {
             while ( message.available() ) {
               //char valIn = message.readChar();         
               Serial.println("GOT Time Set message");
               //rtc.setDateDs1307(45,25,19,6,20,11,26);
             } 
              
          }
          else {
             Serial.print("Got a message:"); 
             Serial.println(message.readChar());
          }
       }
      
}
 
 
 
  //TODO: Add Flash management or config.
  //Test writing to Flash
 /* value = EEPROM.read(0);
  Serial.print("EEPROM TEst1 ");
  Serial.print(0);
  Serial.print("\t");
  Serial.print(value);
  Serial.println();
  EEPROM.write(0,69);*/

  //EEPROM Test
//  value = EEPROM.read(0);
//  Serial.print("EEPROM TEst2 ");
//  Serial.print(0);
//  Serial.print("\t");
//  Serial.print(value);
//  Serial.println();

//Set up Temp Thresholds
    //    int tHthresh = temp.getTemp(ACCESS_TH);
    //    Serial.print("High threshold = ");
    //    Serial.println(tHthresh);
    //    int tLthresh = temp.getTemp(ACCESS_TL);
    //    Serial.print("Low threshold = ");
    //    Serial.println(tLthresh);
    
 //Checking the threshold alarm pin.
         // if(digitalRead(PIN2))
        //   Serial.println("** PIN2 == true **");
        // else
        //   Serial.println("** PIN@ == false **");
      
        //  delay(1000);
