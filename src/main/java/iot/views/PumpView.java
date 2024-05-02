package iot.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iot.mqtt.MQTT;

@PageTitle("Pump")
@Route(value = "/pump", layout = MainLayout.class)
public class PumpView extends HorizontalLayout {

    private NumberField numberField;
    private Button activate;

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

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, numberField, activate);

        add(numberField, activate);
    }

}
