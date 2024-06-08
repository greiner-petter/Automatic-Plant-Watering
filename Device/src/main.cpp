#include <Arduino.h>

#include "MQTT.h"
#include "BMP.h"
#include "Moisture.h"
#include "Pump.h"
#include "Strategy.h"

void loop() {}

void afterSetup();

void setup()
{
    Serial.begin(9600);

    Pump::Init();
    BMP::Init();
    MQTT::Connect();

    // Send Status Online
    MQTT::Publish("devices/project/status", "online", 2, true);

    afterSetup();
}

void afterSetup()
{
    MQTT::Publish("devices/project/temp", BMP::GetTemperature(), 2, false);
    MQTT::Publish("devices/project/pressure", BMP::GetPressure(), 2, false);
    MQTT::Publish("devices/project/altitude", BMP::GetAltitude(), 2, false);
    MQTT::Publish("devices/project/moisture", Moisture::GetMoisture(), 2, false);
    Strategy::DoStrategy();
    
    // Ensure all messages are send
    delay(1000);

    MQTT::Disconnect();

    esp_sleep_enable_timer_wakeup(Strategy::delayBetweenPumpsInMillis * 1000);
    esp_deep_sleep_start();
}
