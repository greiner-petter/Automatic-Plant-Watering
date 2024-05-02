package iot.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTT {

    public static void init() {

        String topic        = "#";
        int qos             = 2;
        String broker       = "tcp://141.41.35.170:1883";
        String clientId     = "Java";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            sampleClient.subscribe(topic, qos, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println(mqttMessage.toString());
                }
            });
            System.out.println("Subscribed");

            // LOOP FOREVER
            //while (sampleClient.isConnected());

            //System.out.println("Shutdown");
        } catch(MqttException me) {
            me.printStackTrace();
        }
    }
}