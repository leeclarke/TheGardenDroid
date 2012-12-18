/*
  Sensor.h - Library for modeling Sensors attached to the Arduino.
  Created by Lee Clarke, Oct 20, 2010.
  Released into the public domain.
*/
#ifndef Sensor_h
#define Sensor_h

#include "Arduino.h"

class Sensor
{
  public:
    Sensor(int sensorId, String name, unsigned long pollInterval);
    
    void setLowThreshold(int value);
    void setHighThreshold(int value);
    void setPollInterval(unsigned long mills);
    unsigned long getPollInterval();
    int getSensorValue();
    int getSensorState();
    int getSensorID();
    String getSensorName();
    char check();
    void reset();
  protected:
    int _lowThresh;
    int _highThresh;
    int _id;
    String _name;
    unsigned long _pollInterval;
    byte sendStatus;
    int autoreset;
    unsigned long  previous_millis;
};
#endif
