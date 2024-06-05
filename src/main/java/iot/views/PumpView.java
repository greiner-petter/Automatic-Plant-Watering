package iot.views;

import com.influxdb.query.FluxRecord;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iot.influx.Influx;
import iot.mqtt.MQTT;

import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

        // History Log
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.GERMAN).withChronology(Chronology.ofLocale(Locale.GERMAN));
        Grid<PumpRecord> grid = new Grid<>(PumpRecord.class, false);
        grid.addColumn(pumpRecord -> formatter.format(pumpRecord.getKey())).setHeader("Time").setSortable(true);
        grid.addColumn(PumpRecord::getValue).setHeader("Duration");

        List<PumpRecord> pumpRecords = Influx.getPumpHistory(10).stream().map(record -> new PumpRecord(record.getTime(), (Number)record.getValueByKey("_value"))).collect(Collectors.toList());
        grid.setItems(pumpRecords);
        add(grid);
    }

}
