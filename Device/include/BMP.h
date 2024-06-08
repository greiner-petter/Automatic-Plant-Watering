#pragma once

#include <string>
#include <functional>
#include <cstdint>

// Helper Class for dealing with the BMP280 Sensor
class BMP
{
public:
    static void Init();

    static uint32_t GetTemperature();
    static uint32_t GetPressure();
    static uint32_t GetAltitude();
private:
    static uint32_t WakeUpSensorToTakeMetricForcedMeasurement(const std::function<uint32_t(void)>& InLambda);
};