#include "BMP.h"

#include <Adafruit_BMP280.h>

#define BMP_SCK (13)
#define BMP_MISO (12)
#define BMP_MOSI (11)
#define BMP_CS (10)

Adafruit_BMP280 bmp; // I2C
// Adafruit_BMP280 bmp(BMP_CS); // hardware SPI
// Adafruit_BMP280 bmp(BMP_CS, BMP_MOSI, BMP_MISO,  BMP_SCK);

void BMP::Init()
{
    Serial.println(F("BMP280 Forced Mode Test."));

    if (!bmp.begin(0x76))
    {
        Serial.println(F("Could not find a valid BMP280 sensor, check wiring or "
                         "try a different address!"));
        while (1)
            delay(10);
    }

    /* Default settings from datasheet. */
    bmp.setSampling(Adafruit_BMP280::MODE_FORCED,     /* Operating Mode. */
                    Adafruit_BMP280::SAMPLING_X2,     /* Temp. oversampling */
                    Adafruit_BMP280::SAMPLING_X16,    /* Pressure oversampling */
                    Adafruit_BMP280::FILTER_X16,      /* Filtering. */
                    Adafruit_BMP280::STANDBY_MS_500); /* Standby time. */
}

uint32_t BMP::GetTemperature()
{
    return BMP::WakeUpSensorToTakeMetricForcedMeasurement([] ()
    {
        return static_cast<uint32_t>(bmp.readTemperature());
    });
}
uint32_t BMP::GetPressure()
{
    return BMP::WakeUpSensorToTakeMetricForcedMeasurement([] ()
    {
        return static_cast<uint32_t>(bmp.readPressure());
    });
}
uint32_t BMP::GetAltitude()
{
    return BMP::WakeUpSensorToTakeMetricForcedMeasurement([] ()
    {
        return static_cast<uint32_t>(bmp.readAltitude(1013.25)); /* Adjusted to local forecast! */
    });
}

uint32_t BMP::WakeUpSensorToTakeMetricForcedMeasurement(const std::function<uint32_t(void)>& InLambda)
{
    // must call this to wake sensor up and get new measurement data
    // it blocks until measurement is complete
    if (bmp.takeForcedMeasurement())
    {
        return InLambda();
    }
    else
    {
        Serial.println("Forced measurement failed!");
    }

    return 0;
}