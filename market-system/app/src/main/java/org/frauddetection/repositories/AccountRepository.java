package org.frauddetection.repositories;

import org.frauddetection.models.domain.Account;
import org.neo4j.driver.Session;
import org.neo4j.driver.Driver;

public class AccountRepository {
    private final Driver driver;

    public AccountRepository(Driver driver) {
        this.driver = driver;
    }

    public void save(Account account) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                    """
                    MERGE (a:Account {accountId: $accountId})
                    SET
                        a.accountType = $accountType,
                        a.country = $country,
                        a.balance = $balance,
                        a.createdAt = $createdAt,
                        a.active = $active
                    WITH a
                    MERGE (c:Customer {customerId: $customerId})
                    MERGE (c)-[:OWNS]->(a)
                    """,
                    account.toMap()
                );
                return null;
            });
        };
    };
}
