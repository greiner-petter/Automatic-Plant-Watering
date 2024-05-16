package iot.views;

import com.influxdb.query.FluxRecord;
import com.storedobject.chart.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iot.influx.Influx;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@PageTitle("Dashboard")
@Route(value = "/", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(createChart());

    }

    public SOChart createChart() {
        SOChart soChart = new SOChart();
        soChart.setSize("100%", "550px");

        XAxis xAxis = new XAxis(DataType.DATE);
        xAxis.setDivisions(4);
        xAxis.setName("Time");

        YAxis yAxis = new YAxis(DataType.NUMBER);
        yAxis.setName("Value");

        RectangularCoordinate rc = new RectangularCoordinate(xAxis, yAxis);

        /* Temperature */
        {
            TimeData xValues = new TimeData();
            Data yValues = new Data();

            List<FluxRecord> results = Influx.getTemps();

            LineChart lineData = sampleData(xValues, yValues, results);
            lineData.setName("Temperature");

            lineData.plotOn(rc);
            soChart.add(lineData);
        }
        /* Altitude */
        {
            TimeData xValues = new TimeData();
            Data yValues = new Data();

            List<FluxRecord> results = Influx.getAltitude();
            LineChart lineData = sampleData(xValues, yValues, results);
            lineData.setName("Altitude");

            lineData.plotOn(rc);
            soChart.add(lineData);
        }
        /* Pressure */
        {
            TimeData xValues = new TimeData();
            Data yValues = new Data();

            List<FluxRecord> results = Influx.getPressure();
            LineChart lineData = sampleData(xValues, yValues, results);
            lineData.setName("Pressure");

            lineData.plotOn(rc);
            soChart.add(lineData);
        }
        /* Moisture */
        {
            TimeData xValues = new TimeData();
            Data yValues = new Data();

            List<FluxRecord> results = Influx.getMoisture();
            LineChart lineData = sampleData(xValues, yValues, results);
            lineData.setName("Moisture");

            lineData.plotOn(rc);
            soChart.add(lineData);

        }

        return soChart;
    }

    private static LineChart sampleData(TimeData xValues, Data yValues, List<FluxRecord> results) {
        if (results != null) {
            for (FluxRecord result : results) {
                xValues.add(LocalDateTime.ofInstant(result.getTime(), ZoneId.systemDefault()));
                yValues.add((Number) result.getValueByKey("_value"));
            }
        } else {
            System.out.println("No Chart data.");
        }

        return new LineChart(xValues, yValues);
    }

}
