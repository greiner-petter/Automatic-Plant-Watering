package iot.views;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PumpRecord {

    public LocalDateTime key;

    public Number value;

    public PumpRecord(Instant time, Number value) {
        this.key = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
        this.value = value;
    }

    public LocalDateTime getKey() {
        return key;
    }

    public void setKey(LocalDateTime key) {
        this.key = key;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }
}
