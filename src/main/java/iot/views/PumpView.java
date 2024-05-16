package iot.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iot.mqtt.MQTT;

@PageTitle("Pump")
@Route(value = "/pump", layout = MainLayout.class)
public class PumpView extends VerticalLayout {

    private NumberField numberField;
    private Button activate;
    private Button revoke;

    public PumpView() {
        numberField = new NumberField("Duration");
        numberField.setValue(2000.0);
        numberField.setMin(100);
        numberField.setMax(60_000);
        activate = new Button("Activate");
        activate.addClickListener(e -> {
            if (!numberField.isInvalid()) {
                MQTT.activatePump(numberField.getValue().intValue());
            }
        });
        activate.addClickShortcut(Key.ENTER);
        activate.setTooltipText("Active the Pump with a given duration in the next cycle.");

        revoke = new Button("Revoke", VaadinIcon.BACKWARDS.create());
        revoke.addClickListener(e -> {
            MQTT.cancelPump();
        });
        revoke.setTooltipText("Just use the Strategy in the next cycle.");

        add(numberField, activate, revoke);
    }

}
