package org.frauddetection.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.frauddetection.models.domain.Device;
import org.frauddetection.repositories.DeviceRepository;

public class DeviceGenerator {
    private static final List<String> DEVICE_TYPES = List.of("ANDROID", "IOS", "WEB_BROWSER", "POS_TERMINAL");

    private final Random random;
    private final DeviceRepository deviceRepository;

    public DeviceGenerator(DeviceRepository deviceRepository, Random random) {
        this.deviceRepository = deviceRepository;
        this.random = random;
    }

    public Device generate() {
        Device device = new Device(
            "dev-" + UUID.randomUUID(),
            DEVICE_TYPES.get(random.nextInt(DEVICE_TYPES.size()))
        );
        deviceRepository.save(device);
        return device;
    }

    public List<Device> generateMany(int count) {
        List<Device> devices = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            devices.add(generate());
        }
        return devices;
    }
}
