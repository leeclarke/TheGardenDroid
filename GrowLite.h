/*
  GrowLite.h - While not technically a sensor a grow lite is powered by a Relay which has much
  the same functionality contained in the Sensor class therefore it is a child.
  Created by Lee Clarke, Nov 05, 2010.
  Released into the public domain.
*/
#ifndef GrowLite_h
#define GrowLite_h

class GrowLite  {
public:
  GrowLite(int sensorId, String name, int pinLocation);
  int getStatus();
  String getStatusDesc();
  void setDigitalPin(int pin);
  int getDigitalPin();
  void setStartTime(int hour, int minute);
  void setEndTime(int hour, int minute);
  String getStartTimeStr();
  String getEndTimeStr();
  int checkTime(int hour, int minute);
private:
  int pin;
  int status;
  int startHour, startMin;
  int endHour, endMin;
  
};
#endif
