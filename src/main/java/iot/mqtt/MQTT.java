package iot.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTT {

    private static MqttClient mqttClient;

    public static void init() {

        final String topic        = "#";
        final int qos             = 2;
        final String broker       = "tcp://141.41.35.170:1883";
        final String clientId     = "Java";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            mqttClient.connect(connOpts);
            mqttClient.subscribe(topic, qos, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println(mqttMessage.toString());
                }
            });
            System.out.println("Subscribed");

        } catch(MqttException me) {
            me.printStackTrace();
        }
    }
    public static void activatePump(int duration) {
        try {
            mqttClient.publish("event/pump", String.valueOf(duration).getBytes(), 2, false);
        } catch(MqttException me) {
            me.printStackTrace();
        }
    }
}