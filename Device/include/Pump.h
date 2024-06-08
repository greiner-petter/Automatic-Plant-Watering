#pragma once

#include <cstdint>

class Pump
{
public:
    static void Init();
    static void ActivateForDuration(uint32_t durationInMillis);
};