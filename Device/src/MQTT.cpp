#include "MQTT.h"

#include <WiFi.h>
#include <mqtt_client.h>
#include "secrets.h"
#include "config.h"

#include "Pump.h"
#include "Strategy.h"

extern const char ca_crt_start[] asm("_binary_data_ca_crt_start");
extern const char ca_crt_end[] asm("_binary_data_ca_crt_end");
extern const char client_crt_start[] asm("_binary_data_client_crt_start");
extern const char client_crt_end[] asm("_binary_data_client_crt_end");
extern const char client_key_start[] asm("_binary_data_client_key_start");
extern const char client_key_end[] asm("_binary_data_client_key_end");

static esp_mqtt_client_handle_t s_Client;

static esp_err_t mqtt_event_callback(esp_mqtt_event_handle_t event)
{
    if (event->event_id == MQTT_EVENT_DATA)
    {
        Serial.println("MQTT Event: DATA Received");
        Serial.print("Topic: ");
        std::string topic = std::string(event->topic, event->topic_len);
        Serial.println(topic.c_str());

        Serial.print("Message: ");
        std::string message = std::string(event->data, event->data_len);
        Serial.println(message.c_str());

        MQTT::HandleRetainedEvents(topic, message);
    }

    return {};
}

void MQTT::HandleRetainedEvents(std::string topic, std::string message)
{
    if (topic == "event/pump")
    {
        Strategy::pumpOnce = static_cast<EPumpDuration>(std::abs(std::atoi(message.c_str())));
    }
    if (topic == "event/strategy/threshold")
    {
        Strategy::moistureThreshold = static_cast<EThreshold>(std::abs(std::atoi(message.c_str())));
    }
    if (topic == "event/strategy/duration")
    {
        Strategy::pumpDuration = static_cast<EPumpDuration>(std::abs(std::atoi(message.c_str())));
    }
}

void MQTT::Connect()
{
    WiFi.mode(WIFI_STA);
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

    if (WiFi.waitForConnectResult() != WL_CONNECTED)
    {
        Serial.println("WiFi connection failed!");
    }
    else
    {
        Serial.print("WiFi connected. IP-Adress: ");
        Serial.println(WiFi.localIP());
    }

    esp_mqtt_client_config_t config = esp_mqtt_client_config_t();
    config.host = MQTT_HOSTNAME;
    config.port = MQTT_PORT;

    // Security
    config.client_id = "esp";
    config.transport = MQTT_TRANSPORT_OVER_SSL; // SSL
    config.cert_pem = ca_crt_start; // ca.crt file
    config.client_cert_pem = client_crt_start; // client.crt file
    config.client_key_pem = client_key_start; // client.key file

    // Setup Last Will & Testament
    config.lwt_retain = 1;
    config.lwt_msg = "offline";
    config.lwt_qos = 2;
    config.lwt_topic = "devices/project/status";
    config.keepalive = 10;
    // config.disable_auto_reconnect = 1;

    // Setip Event Callback
    config.event_handle = &mqtt_event_callback;

    s_Client = esp_mqtt_client_init(&config);
    if (s_Client == NULL)
    {
        Serial.println("MQTT Client Init Error");
    }
    if (esp_mqtt_client_start(s_Client) != ESP_OK)
    {
        Serial.println("MQTT Client Start Error");
    }

    Serial.println("MQTT Client Connected!");

    // Subscribe to all 'event/#'
    esp_mqtt_client_subscribe(s_Client, "event/#", 2);
}

void MQTT::Disconnect()
{
    Serial.println("MQTT Disconneted.");
    esp_mqtt_client_disconnect(s_Client);
}

void MQTT::Publish(const std::string &topic, const std::string &data, int32_t qos, bool retain, bool enqueue)
{
    if (!enqueue)
    {
        if (esp_mqtt_client_publish(s_Client, topic.c_str(), data.c_str(), 0, qos, retain ? 1 : 0) == -1)
        {
            Serial.println("MQTT::Publish(): Something went wrong.");
        }
    }
    else
    {
        if (esp_mqtt_client_enqueue(s_Client, topic.c_str(), data.c_str(), 0, qos, retain ? 1 : 0, 1) == -1)
        {
            Serial.println("MQTT::Enqueue(): Something went wrong.");
        }
    }
}
void MQTT::Publish(const std::string &topic, int32_t data, int32_t qos, bool retain, bool enqueue)
{
    Publish(topic, std::to_string(data), qos, retain, enqueue);
}
