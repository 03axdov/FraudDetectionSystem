package org.frauddetection.repositories;

import org.frauddetection.models.domain.IpAddress;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

public class IpAddressRepository {
    private final Driver driver;

    public IpAddressRepository(Driver driver) {
        this.driver = driver;
    }

    public void save(IpAddress ipAddress) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                    """
                    MERGE (ip:IpAddress {ipAddress: $ipAddress})
                    """,
                    ipAddress.toMap()
                );
                return null;
            });
        };
    };
}
