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

public class Influx {

    private static InfluxDBClient influxDBClient;

    public static void init() {
        String url = "http://localhost:8086";
        String token = getInfluxTokenFromProxmox();
        System.out.println("Influx Token: '" + token + "'");

        String org = "my-org";
        String bucket = "telegraf";
        influxDBClient = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
        System.out.println("Influx Connection: " + influxDBClient.ping());
    }

    private static String getInfluxTokenFromProxmox() {
        try {
            String content = Files.readString(new File("/home/iot-projekt/git/IoT-Webserver/influxdb/influxdb-config/influx-configs").toPath());
            String tokenLine = content.split( "\n")[2];
            return tokenLine.substring(11, 99);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This is the last used Token.
        // We use it as a fallback token
        return "XUDcWydFHk6viOK3Ls6Kn7TvVUjoX30OPoGbRhtad7pOqj2HISJ8hZd6A14WKx10k_zIvw_mMhpFndMRwr0fgw==";
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
    private static List<FluxRecord> queryTopic(String topic) {
        String flux = "from(bucket: \"telegraf\")\n" +
                "  |> range(start: -1h)\n" +
                "  |> filter(fn: (r) => r[\"_measurement\"] == \"mqtt_consumer\")\n" +
                "  |> filter(fn: (r) => r[\"_field\"] == \"value\")\n" +
                "  |> filter(fn: (r) => r[\"topic\"] == \""+topic+"\")\n" +
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