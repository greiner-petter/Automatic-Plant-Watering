package iot.views.simulation;

import iot.mqtt.MQTT;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SimulationConfig {

    public static boolean simulate = false;
    public static Double temperature = 21.0;

    @Scheduled(fixedDelay = 1000)
    public void scheduleSimulateTick() {
        if (simulate) {
            System.out.println("Currently Simulating Device");
            MQTT.publish("devices/project/temp", String.valueOf(temperature.intValue()));
        }
    }

}