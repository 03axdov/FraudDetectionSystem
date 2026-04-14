package org.frauddetection.repositories;

import org.frauddetection.models.events.Transaction;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

public class TransactionRepository {
    private final Driver driver;

    public TransactionRepository(Driver driver) {
        this.driver = driver;
    }

    public void save(Transaction transaction) {
        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                tx.run(
                    """
                    MERGE (t:Transaction {transactionId: $transactionId})
                    SET
                        t.timestamp = $timestamp,
                        t.amount = $amount,
                        t.currency = $currency,
                        t.transactionType = $transactionType,
                        t.status = $status,
                        t.channel = $channel,
                        t.country = $country,
                        t.city = $city
                    WITH t
                    MERGE (from:Account {accountId: $fromAccountId})
                    MERGE (t)-[:FROM_ACCOUNT]->(from)
                    WITH t
                    MERGE (to:Account {accountId: $toAccountId})
                    MERGE (t)-[:TO_ACCOUNT]->(to)
                    WITH t
                    MERGE (d:Device {deviceId: $deviceId})
                    MERGE (t)-[:USING_DEVICE]->(d)
                    WITH t
                    MERGE (ip:IpAddress {ipAddress: $ipAddress})
                    MERGE (t)-[:FROM_IP]->(ip)
                    WITH t
                    MERGE (m:Merchant {merchantId: $merchantId})
                    MERGE (t)-[:AT_MERCHANT]->(m)
                    """,
                    transaction.toMap()
                );
                return null;
            });
        };
    };
}
