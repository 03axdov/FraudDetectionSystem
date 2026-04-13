package org.frauddetection;

import org.neo4j.driver.GraphDatabase;
import org.frauddetection.models.domain.Customer;
import org.frauddetection.repositories.CustomerRepository;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import java.time.Instant;
import org.frauddetection.models.enums.BehaviorProfile;

public class App {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver(
            System.getenv("NEO4J_URI"),
            AuthTokens.basic(
                System.getenv("NEO4J_USERNAME"),
                System.getenv("NEO4J_PASSWORD")
            )
        );

        try {
            CustomerRepository customerRepository = new CustomerRepository(driver);
        
            Customer testCustomer = new Customer(
                "1283947832409",
                "Axel",
                "HIGH",
                Instant.now(),
                BehaviorProfile.BURST_FRAUDSTER
            );

            customerRepository.save(testCustomer);
        } finally {
            driver.close();
        }
        
    }
}
