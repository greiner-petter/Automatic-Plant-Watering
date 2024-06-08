#include <Arduino.h>
#include "Moisture.h"

#define AOUT_PIN 36 // ESP32 pin GPIO36 (ADC0) that connects to AOUT pin of moisture sensor
#define THRESHOLD 2800 // CHANGE YOUR THRESHOLD HERE

// Wet  - Dry
// 2600 - 3100
uint32_t Moisture::GetMoisture()
{
    return analogRead(AOUT_PIN);
}

bool Moisture::IsWet()
{
    return GetMoisture() <= THRESHOLD;
}