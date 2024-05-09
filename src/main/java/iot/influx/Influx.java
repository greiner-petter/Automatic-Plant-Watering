package iot.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import java.util.List;

public class Influx {

    private static InfluxDBClient influxDBClient;

    public static void init() {
        String url = "http://localhost:8086";
        String token = "ckitZbCD8MwLYcIvacKCW-h5eZjVBBJujImg93EoO2ulUZwHKI6I9-E-g2GVd3zXeiIKx2Qg8QK7d3lIPRvOqA==";
        String org = "my-org";
        String bucket = "telegraf";
        influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        System.out.println("Influx Ping: " +  influxDBClient.ping());
    }

    public static List<FluxRecord> getTemps() {
        String flux = "from(bucket: \"telegraf\")\n" +
                "  |> range(start: -1h)\n" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"mqtt_consumer\")\n" +
                "  |> filter(fn: (r) => r[\"_field\"] == \"value\")\n" +
                "  |> filter(fn: (r) => r[\"host\"] == \"170a5e755157\")\n" +
                "  |> filter(fn: (r) => r[\"topic\"] == \"devices/project/temp\")\n" +
                "  |> aggregateWindow(every: 1m, fn: mean, createEmpty: false)\n" +
                "  |> yield(name: \"mean\")";

        QueryApi queryApi = influxDBClient.getQueryApi();

        List<FluxTable> tables = queryApi.query(flux);
        for (FluxTable fluxTable : tables) {
            return fluxTable.getRecords();
        }
        return null;
    }

}