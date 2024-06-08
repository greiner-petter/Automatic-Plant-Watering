#pragma once

#include <string>
#include <vector>
#include <functional>

class MQTT
{
public:
    static void Connect();
    static void Disconnect();

    static void Publish(const std::string& topic, const std::string& data, int32_t qos, bool retain, bool enqueue = false);
    static void Publish(const std::string& topic, int32_t            data, int32_t qos, bool retain, bool enqueue = false);

    static void HandleRetainedEvents(std::string topic, std::string message);
};