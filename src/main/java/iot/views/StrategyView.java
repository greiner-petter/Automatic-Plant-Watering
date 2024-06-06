package iot.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iot.mqtt.MQTT;

@PageTitle("Strategy")
@Route(value = "/strategy", layout = MainLayout.class)
public class StrategyView extends VerticalLayout {

    public static Double globalDuration = 2000.0;
    public static Double globalThreshold = 2900.0;
    public static Double globalSleep = 1000.0 * 60 * 5;

    private NumberField durationNumberField;
    private NumberField thresholdNumberField;
    private NumberField sleepNumberField;
    private Button upload;

    public StrategyView() {
        durationNumberField = new NumberField("Pump Duration");
        durationNumberField.setValue(globalDuration);
        durationNumberField.setMin(100);
        durationNumberField.setMax(60_000);
        Button durationLow = new Button("Low");
        durationLow.addClickListener(event -> durationNumberField.setValue(1500.0));
        Button durationMedium = new Button("Medium");
        durationMedium.addClickListener(event -> durationNumberField.setValue(2000.0));
        Button durationHigh = new Button("High");
        durationHigh.addClickListener(event -> durationNumberField.setValue(3000.0));

        thresholdNumberField = new NumberField("Moisture Threshold");
        thresholdNumberField.setValue(globalThreshold);
        thresholdNumberField.setMin(2000.0);
        thresholdNumberField.setMax(3500.0);
        Button thresholdLow = new Button("Low");
        thresholdLow.addClickListener(event -> thresholdNumberField.setValue(2800.0));
        Button thresholdMedium = new Button("Medium");
        thresholdMedium.addClickListener(event -> thresholdNumberField.setValue(2900.0));
        Button thresholdHigh = new Button("High");
        thresholdHigh.addClickListener(event -> thresholdNumberField.setValue(3000.0));

        sleepNumberField = new NumberField("Sleep Duration");
        sleepNumberField.setValue(globalSleep);
        sleepNumberField.setMin(1000.0);

        upload = new Button("Upload Strategy", VaadinIcon.UPLOAD.create());
        upload.addClickListener(e -> {
            if (!durationNumberField.isInvalid()) {
                globalDuration = durationNumberField.getValue();
                MQTT.publish("event/strategy/duration", globalDuration.toString(), 2, true);
                Notification.show("Duration uploaded", 4000, Notification.Position.BOTTOM_CENTER);
            }
            if (!thresholdNumberField.isInvalid()) {
                globalThreshold = thresholdNumberField.getValue();
                MQTT.publish("event/strategy/threshold", globalThreshold.toString(), 2, true);
                Notification.show("Threshold uploaded", 4000, Notification.Position.BOTTOM_CENTER);
            }
            if (!sleepNumberField.isInvalid()) {
                globalSleep = sleepNumberField.getValue();
                MQTT.publish("event/strategy/sleep", globalSleep.toString(), 2, true);
                Notification.show("Sleep Duration uploaded", 4000, Notification.Position.BOTTOM_CENTER);
            }
        });
        upload.addClickShortcut(Key.ENTER);
        upload.setTooltipText("Upload your Strategy to be used in the next cycle.");

        add(durationNumberField, new HorizontalLayout(durationLow, durationMedium, durationHigh), thresholdNumberField, new HorizontalLayout(thresholdLow, thresholdMedium, thresholdHigh), sleepNumberField, upload);
    }

}
