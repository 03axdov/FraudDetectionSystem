package org.frauddetection.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.frauddetection.models.domain.Device;

public class DeviceGenerator {
    private static final List<String> DEVICE_TYPES = List.of("ANDROID", "IOS", "WEB_BROWSER", "POS_TERMINAL");

    private final Random random;

    public DeviceGenerator() {
        this(new Random());
    }

    public DeviceGenerator(Random random) {
        this.random = random;
    }

    public Device generate() {
        return new Device(
            "dev-" + UUID.randomUUID(),
            DEVICE_TYPES.get(random.nextInt(DEVICE_TYPES.size()))
        );
    }

    public List<Device> generateMany(int count) {
        List<Device> devices = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            devices.add(generate());
        }
        return devices;
    }
}
