package org.frauddetection.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.frauddetection.models.domain.Merchant;

public class MerchantGenerator {
    private static final List<String> CATEGORIES = List.of(
        "GROCERY", "ELECTRONICS", "TRAVEL", "RESTAURANT", "FASHION", "MARKETPLACE", "FUEL", "HEALTH"
    );
    private static final List<String> COUNTRIES = List.of("DE", "NL", "FR", "ES", "IT", "US", "GB", "SE");

    private final Random random;

    public MerchantGenerator() {
        this(new Random());
    }

    public MerchantGenerator(Random random) {
        this.random = random;
    }

    public Merchant generate() {
        return new Merchant(
            "mrc-" + UUID.randomUUID(),
            CATEGORIES.get(random.nextInt(CATEGORIES.size())),
            COUNTRIES.get(random.nextInt(COUNTRIES.size()))
        );
    }

    public List<Merchant> generateMany(int count) {
        List<Merchant> merchants = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            merchants.add(generate());
        }
        return merchants;
    }
}
