package iot.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTT {

    public static void init() {

        String topic        = "gruppe-n/mqtt/test";
        String content      = "Message from Java";
        String lastWill     = "This is my last Will.";
        int qos             = 2;
        String broker       = "tcp://localhost:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            sampleClient.subscribe(topic, qos, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage)
                        throws Exception {
                    System.out.println(mqttMessage.toString());
                }
            });
            System.out.println("Subscribed");

            // LOOP FOREVER
            while (sampleClient.isConnected());

            System.out.println("Shutdown");
            System.exit(0);
        } catch(MqttException me) {
            me.printStackTrace();
        }
    }
}