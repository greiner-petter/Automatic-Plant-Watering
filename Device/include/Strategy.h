#pragma once

#include <cstdint>

enum class EPumpDuration : int32_t
{
    SHORT = 1000,
    MEDIUM = 1500,
    LONG = 2000    
};
enum class EThreshold : int32_t
{
    AGRESSIVE = 2800,
    NORMAL = 2900,
    LAZY = 3000    
};

struct Strategy
{
    inline static int64_t delayBetweenPumpsInMillis = 1000 * 60 * 1; // 1 Second * 60 * 1 = 1 Minute
    inline static EPumpDuration pumpDuration = EPumpDuration::MEDIUM;
    inline static EThreshold moistureThreshold = EThreshold::NORMAL;
    inline static EPumpDuration pumpOnce = (EPumpDuration)0;

    static void DoStrategy();
    static bool ShouldPump();
};