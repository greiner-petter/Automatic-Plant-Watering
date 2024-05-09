package iot.views;

import com.influxdb.query.FluxRecord;
import com.storedobject.chart.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import iot.influx.Influx;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@PageTitle("Dashboard")
@Route(value = "/", layout = MainLayout.class)
public class DashboardView extends HorizontalLayout {

    public DashboardView() {
        SOChart soChart = new SOChart();
        soChart.setSize("100%", "500px");
        String title = "Temperature";
        soChart.add(new Title(title));

        XAxis xAxis = new XAxis(DataType.DATE);
        xAxis.setDivisions(10);
        xAxis.setName("Time");

        YAxis yAxis = new YAxis(DataType.NUMBER);
        yAxis.setName("°C");

        RectangularCoordinate rc = new RectangularCoordinate(xAxis, yAxis);

        /*for ()*/ {
            TimeData xValues = new TimeData();
            Data yValues = new Data();

            List<FluxRecord> results = Influx.getTemps();
            if (results != null) {
                for (FluxRecord result : results) {
                    xValues.add(LocalDateTime.ofInstant(result.getTime(), ZoneId.systemDefault()));
                    yValues.add((Number)result.getValueByKey("_value"));
                }
            } else {
                System.out.println("No Chart data.");
            }

            LineChart lineData = new LineChart(xValues, yValues);
            lineData.setName("Temperature");

            lineData.plotOn(rc);
            soChart.add(lineData);
        }

        add(soChart);
    }

}
