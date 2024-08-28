package com.ericsson.oss.apps.config;

public enum CounterName {
    NR_EVENTS_PARSED("event.nr.parsed"),
    NR_EVENTS_SUCC_PARSED("event.nr.parsed.succ"),
    NR_EVENTS_BYTES_PARSED("event.nr.parsed.bytes"),
    ;

    private final String name;

    CounterName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
