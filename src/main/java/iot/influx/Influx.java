package iot.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Influx {

    private static InfluxDBClient influxDBClient;

    public static void init() {
        String url = "http://localhost:8086";
        String token = "super-secret-auth-token";
        System.out.println("Influx Token: '" + token + "'");

        String org = "my-org";
        String bucket = "telegraf";
        influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        System.out.println("Influx Connection: " + influxDBClient.ping());
    }

    public static List<FluxRecord> getPumpHistory(int maxCount) {
        return Objects.requireNonNull(queryTopic("devices/project/pumpForDuration")).stream().limit(maxCount).collect(Collectors.toList());
    }
    public static List<FluxRecord> getTemps() {
        return queryTopic("devices/project/temp");
    }

    public static List<FluxRecord> getAltitude() {
        return queryTopic("devices/project/altitude");
    }

    public static List<FluxRecord> getPressure() {
        return queryTopic("devices/project/pressure");
    }

    public static List<FluxRecord> getMoisture() {
        return queryTopic("devices/project/moisture");
    }

    private static List<FluxRecord> queryTopic(String topic) {
        String flux = "from(bucket: \"telegraf\")\n" +
                "  |> range(start: -1h)\n" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"mqtt_consumer\")\n" +
                "  |> filter(fn: (r) => r[\"_field\"] == \"value\")\n" +
                "  |> filter(fn: (r) => r[\"topic\"] == \"" + topic + "\")\n" +
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