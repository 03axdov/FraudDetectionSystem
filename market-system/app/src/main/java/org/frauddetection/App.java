package org.frauddetection;

import org.neo4j.driver.GraphDatabase;

import org.frauddetection.simulator.MarketSimulator;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;

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
            MarketSimulator marketSimulator = new MarketSimulator(driver);
            marketSimulator.simulateMarket(25, 3, 200);
        } finally {
            driver.close();
        }
        
    }
}
