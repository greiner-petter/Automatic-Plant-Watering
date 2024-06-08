#include "Strategy.h"
#include "MQTT.h"
#include "Moisture.h"
#include "Pump.h"

#include "Arduino.h"

bool Strategy::ShouldPump()
{
    return Moisture::GetMoisture() > static_cast<int32_t>(moistureThreshold);
}

void Strategy::DoStrategy()
{
    if (static_cast<int32_t>(pumpOnce))
    {
        Serial.print("PumpOnce: "); Serial.println(static_cast<int32_t>(pumpOnce));
        Pump::ActivateForDuration(static_cast<int32_t>(pumpOnce));
        pumpOnce = (EPumpDuration)0;
        MQTT::Publish("event/pump", "", 2, true, true);
    }
    else
    {
        if (ShouldPump())
        {
            Pump::ActivateForDuration(static_cast<int32_t>(pumpDuration));
        }
    }

}