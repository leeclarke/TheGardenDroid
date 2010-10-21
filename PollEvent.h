#ifndef PollEvent_h
#define PollEvent_h

class PollEvent
{

public:
  PollEvent();
  PollEvent(unsigned long interval_millis);
  //PollEvent(unsigned long interval_millis, int autoreset);
  void interval(unsigned long interval_millis);
  char check();
  void reset();
	
private:
  int autoreset;
  unsigned long  previous_millis, interval_millis;

};

#endif

