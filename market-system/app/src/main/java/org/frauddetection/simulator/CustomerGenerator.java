package org.frauddetection.simulator;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.frauddetection.models.domain.Customer;
import org.frauddetection.models.enums.BehaviorProfile;

public class CustomerGenerator {
    private static final List<String> FIRST_NAMES = List.of(
        "Alex", "Jordan", "Taylor", "Morgan", "Sam", "Casey", "Riley", "Avery", "Jamie", "Cameron"
    );
    private static final List<String> LAST_NAMES = List.of(
        "Miller", "Anderson", "Garcia", "Nguyen", "Brown", "Smith", "Martin", "Johnson", "Clark", "Lopez"
    );

    private final Random random;

    public CustomerGenerator() {
        this(new Random());
    }

    public CustomerGenerator(Random random) {
        this.random = random;
    }

    public Customer generate() {
        return new Customer(
            "cust-" + UUID.randomUUID(),
            randomName(),
            randomCreatedAt(),
            weightedRandomProfile()
        );
    }

    private BehaviorProfile weightedRandomProfile() {
        BehaviorProfile[] values = BehaviorProfile.values();

        double[] weights = {
            0.4,  // NORMAL
            0.2,  // HIGH_SPENDER
            0.2,  // SMALL_MERCHANT
            0.1,  // MULE_ACCOUNT
            0.05, // FRAUD_RING_MEMBER
            0.05  // BURST_FRAUDSTER
        };

        double total = 0;
        for (double w : weights) {
            total += w;
        }

        double r = this.random.nextDouble() * total;

        double cumulative = 0;
        for (int i = 0; i < values.length; i++) {
            cumulative += weights[i];
            if (r <= cumulative) {
                return values[i];
            }
        }

        throw new IllegalStateException("Should never reach here");
    }


    private String randomName() {
        return FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size())) + " "
            + LAST_NAMES.get(random.nextInt(LAST_NAMES.size()));
    }

    private Instant randomCreatedAt() {
        long daysAgo = random.nextInt(365 * 3 + 1);
        long minutesOffset = random.nextInt(24 * 60);
        return Instant.now()
            .minus(daysAgo, ChronoUnit.DAYS)
            .minus(minutesOffset, ChronoUnit.MINUTES);
    }
}
