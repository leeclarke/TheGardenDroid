#include "WProgram.h"
#include "PollEvent.h"

PollEvent::PollEvent()
{
  reset();
}

PollEvent::PollEvent(unsigned long interval_millis)
{
  interval(interval_millis);
  reset();
}

void PollEvent::interval(unsigned long interval_millis)
{
  this->interval_millis = interval_millis;
}

char PollEvent::check()
{
  if (millis() - this->previous_millis >= this->interval_millis) {
    
    if (this->interval_millis == 0 || this->autoreset ) {
      this->previous_millis = millis();
    } 
    else {
      this->previous_millis += this->interval_millis; 
    }

    return 1;
  }
  return 0;
}

void PollEvent::reset() 
{
  this->previous_millis = millis();
}

