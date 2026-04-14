package org.frauddetection.repositories;

import org.frauddetection.models.domain.Device;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

public class DeviceRepository {
    private final Driver driver;

    public DeviceRepository(Driver driver) {
        this.driver = driver;
    }

    public void save(Device device) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                    """
                    MERGE (d:Device {deviceId: $deviceId})
                    SET
                        d.deviceType = $deviceType
                    """,
                    device.toMap()
                );
                return null;
            });
        };
    };
}
