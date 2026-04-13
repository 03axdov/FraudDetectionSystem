package org.frauddetection.repositories;

import org.frauddetection.models.domain.Customer;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import java.util.Map;


public class CustomerRepository {
    private final Driver driver;

    public CustomerRepository(Driver driver) {
        this.driver = driver;
    }

    public void save(Customer customer) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                Result result = tx.run(
                    """
                    MERGE (c:Customer {customerId: $customerId})
                    SET
                        c.name = $name,
                        c.riskLevel = $riskLevel,
                        c.createdAt = $createdAt,
                        c.behaviorProfile = $behaviorProfile
                    """,
                    customer.toMap()
                );
                return null;
            });
        };
    };
}
