package org.frauddetection.repositories;

import org.frauddetection.models.domain.Merchant;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

public class MerchantRepository {
    private final Driver driver;

    public MerchantRepository(Driver driver) {
        this.driver = driver;
    }

    public void save(Merchant merchant) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                    """
                    MERGE (m:Merchant {merchantId: $merchantId})
                    SET
                        m.category = $category,
                        m.country = $country
                    """,
                    merchant.toMap()
                );
                return null;
            });
        };
    };
}
