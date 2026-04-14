package org.frauddetection.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.frauddetection.models.domain.IpAddress;
import org.frauddetection.repositories.IpAddressRepository;

public class IpAddressGenerator {
    private final Random random;
    private final IpAddressRepository ipAddressRepository;

    public IpAddressGenerator(IpAddressRepository ipAddressRepository, Random random) {
        this.ipAddressRepository = ipAddressRepository;
        this.random = random;
    }

    public IpAddress generate() {
        IpAddress ipAddress = new IpAddress(
            random.nextInt(223) + "."
                + random.nextInt(256) + "."
                + random.nextInt(256) + "."
                + (1 + random.nextInt(254))
        );
        ipAddressRepository.save(ipAddress);
        return ipAddress;
    }

    public List<IpAddress> generateMany(int count) {
        List<IpAddress> ipAddresses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ipAddresses.add(generate());
        }
        return ipAddresses;
    }
}
