package iot.views.simulation;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iot.views.MainLayout;

@PageTitle("Simulation")
@Route(value = "/simulation", layout = MainLayout.class)
public class SimulationView extends VerticalLayout {

    private final Button simulate;

    public SimulationView() {
        NumberField numberFieldTemperature = new NumberField("Simulated Temperature");
        numberFieldTemperature.setValue(SimulationConfig.temperature);
        numberFieldTemperature.setMin(-8.0);
        numberFieldTemperature.setMax(120.0);
        numberFieldTemperature.addValueChangeListener(event -> SimulationConfig.temperature = event.getValue());
        
        simulate = new Button("Simulate");
        simulate.setIcon(SimulationConfig.simulate ? VaadinIcon.STOP.create() : VaadinIcon.PLAY.create());
        simulate.addClickListener(event -> {
            SimulationConfig.simulate = !SimulationConfig.simulate;
            simulate.setIcon(SimulationConfig.simulate ? VaadinIcon.STOP.create() : VaadinIcon.PLAY.create());
        });
        simulate.addClickShortcut(Key.ENTER);

        add(numberFieldTemperature, simulate);
    }

}
