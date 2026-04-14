package org.frauddetection.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.frauddetection.models.domain.IpAddress;

public class IpAddressGenerator {
    private final Random random;

    public IpAddressGenerator() {
        this(new Random());
    }

    public IpAddressGenerator(Random random) {
        this.random = random;
    }

    public IpAddress generate() {
        return new IpAddress(
            random.nextInt(223) + "."
                + random.nextInt(256) + "."
                + random.nextInt(256) + "."
                + (1 + random.nextInt(254))
        );
    }

    public List<IpAddress> generateMany(int count) {
        List<IpAddress> ipAddresses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ipAddresses.add(generate());
        }
        return ipAddresses;
    }
}
