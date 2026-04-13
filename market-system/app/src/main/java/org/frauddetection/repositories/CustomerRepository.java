package org.frauddetection.repositories;

import org.frauddetection.models.domain.Customer;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;


public class CustomerRepository {
    private final Driver driver;

    public CustomerRepository(Driver driver) {
        this.driver = driver;
    }

    public void save(Customer customer) {
        try (Session session = driver.session()) {
            
        };
    };
}
