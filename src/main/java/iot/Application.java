package iot;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import iot.influx.Influx;
import iot.mqtt.MQTT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "iot-dashboard")
@Push
public class Application implements AppShellConfigurator {

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        MQTT.init();
        Influx.init();
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
