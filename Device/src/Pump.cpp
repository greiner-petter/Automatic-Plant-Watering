#include <Arduino.h>
#include "Pump.h"

#include "MQTT.h"

#define PUMP_PIN 12

void Pump::Init()
{
    pinMode(PUMP_PIN, OUTPUT);
}

void Pump::ActivateForDuration(uint32_t durationInMillis)
{
    MQTT::Publish("devices/project/pumpForDuration", durationInMillis, 2, false, true);
    digitalWrite(PUMP_PIN, HIGH);
    delay(durationInMillis);
    digitalWrite(PUMP_PIN, LOW);
}