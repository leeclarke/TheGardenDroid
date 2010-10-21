/****
 * 
 * Remove, replaced with RtcSensor.h
 *
 */
 
/**
 * Sets the date/time on the ds1307,  starts the Clock and defaults to 24Hr clock
 * Assumes you're passing in valid numbers
 *    EX.  second = 45;   minute = 17;   hour = 23;   dayOfWeek = 7;   dayOfMonth = 17;   month = 10;   year = 10;
 *         setDateDs1307(second, minute, hour, dayOfWeek, dayOfMonth, month, year);
 */
//void setDateDs1307(byte second,        // 0-59
//                   byte minute,        // 0-59
//                   byte hour,          // 1-23
//                   byte dayOfWeek,     // 1-7
//                   byte dayOfMonth,    // 1-28/29/30/31
//                   byte month,         // 1-12
//                   byte year)          // 0-99
//{
//   Wire.beginTransmission(RTC_ID);
//   Wire.send(0);
//   Wire.send(decToBcd(second));    // 0 to bit 7 starts the clock
//   Wire.send(decToBcd(minute));
//   Wire.send(decToBcd(hour));      // If you want 12 hour am/pm you need to set
//                                   // bit 6 (also need to change readDateDs1307)
//   Wire.send(decToBcd(dayOfWeek));
//   Wire.send(decToBcd(dayOfMonth));
//   Wire.send(decToBcd(month));
//   Wire.send(decToBcd(year));
//   Wire.endTransmission();
//} 
/**
 *Gets the date and time from the ds1307, setting it to the values provided.
 */
//void getDateDs1307(byte *second, byte *minute, byte *hour, byte *dayOfWeek, byte *dayOfMonth, byte *month, byte *year)
//{
//  // Reset the register pointer
//  Wire.beginTransmission(RTC_ID);
//  Wire.send(0);
//  Wire.endTransmission();
//  Wire.requestFrom(RTC_ID, 7);
//  // A few of these need masks because certain bits are control bits
//  *second     = bcdToDec(Wire.receive() & 0x7f);
//  *minute     = bcdToDec(Wire.receive());
//  *hour       = bcdToDec(Wire.receive() & 0x3f);  // Need to change this if 12 hour am/pm
//  *dayOfWeek  = bcdToDec(Wire.receive());
//  *dayOfMonth = bcdToDec(Wire.receive());
//  *month      = bcdToDec(Wire.receive());
//  *year       = bcdToDec(Wire.receive());
//}
// Convert normal decimal numbers to binary coded decimal
//byte decToBcd(byte val)
//{
//  return ( (val/10*16) + (val%10) );
//}
//// Convert binary coded decimal to normal decimal numbers
//byte bcdToDec(byte val)
//{
//  return ( (val/16*10) + (val%16) );
//}
