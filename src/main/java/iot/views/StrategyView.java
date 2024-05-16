package iot.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iot.mqtt.MQTT;

@PageTitle("Strategy")
@Route(value = "/strategy", layout = MainLayout.class)
public class StrategyView extends VerticalLayout {

    private NumberField durationNumberField;
    private NumberField thresholdNumberField;
    private Button upload;

    public StrategyView() {
        durationNumberField = new NumberField("Pump Duration");
        durationNumberField.setValue(2000.0);
        durationNumberField.setMin(100);
        durationNumberField.setMax(60_000);

        thresholdNumberField = new NumberField("Moisture Threshold");
        thresholdNumberField.setValue(2900.0);
        thresholdNumberField.setMin(2500.0);
        thresholdNumberField.setMax(3500.0);

        upload = new Button("Upload Strategy", VaadinIcon.UPLOAD.create());
        upload.addClickListener(e -> {
            if (!durationNumberField.isInvalid()) {
                MQTT.publish("event/strategy/duration", durationNumberField.getValue().toString(), 2, true);
                Notification.show("Duration uploaded", 4000, Notification.Position.BOTTOM_CENTER);
            }
            if (!thresholdNumberField.isInvalid()) {
                MQTT.publish("event/strategy/threshold", thresholdNumberField.getValue().toString(), 2, true);
                Notification.show("Threshold uploaded", 4000, Notification.Position.BOTTOM_CENTER);
            }
        });
        upload.addClickShortcut(Key.ENTER);
        upload.setTooltipText("Upload your Strategy to be used in the next cycle.");


        add(durationNumberField, thresholdNumberField, upload);
    }

}
