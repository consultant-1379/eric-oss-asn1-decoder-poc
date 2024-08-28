package com.ericsson.oss.apps.config;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomMetrics {
    private final MeterRegistry registry;


    CustomMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public Counter getCounter(CounterName counterName) {
        return registry.counter(counterName.getName(), "app", "eric.oss.asn1.decoder.poc");
    }
}
