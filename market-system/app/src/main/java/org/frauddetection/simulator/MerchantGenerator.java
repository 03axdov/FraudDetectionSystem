package org.frauddetection.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.frauddetection.models.domain.Merchant;
import org.frauddetection.repositories.MerchantRepository;

public class MerchantGenerator {
    private static final List<String> CATEGORIES = List.of(
        "GROCERY", "ELECTRONICS", "TRAVEL", "RESTAURANT", "FASHION", "MARKETPLACE", "FUEL", "HEALTH"
    );
    private static final List<String> COUNTRIES = List.of("DE", "NL", "FR", "ES", "IT", "US", "GB", "SE");

    private final Random random;
    private final MerchantRepository merchantRepository;

    public MerchantGenerator(MerchantRepository merchantRepository, Random random) {
        this.merchantRepository = merchantRepository;
        this.random = random;
    }

    public Merchant generate() {
        Merchant merchant = new Merchant(
            "mrc-" + UUID.randomUUID(),
            CATEGORIES.get(random.nextInt(CATEGORIES.size())),
            COUNTRIES.get(random.nextInt(COUNTRIES.size()))
        );
        merchantRepository.save(merchant);
        return merchant;
    }

    public List<Merchant> generateMany(int count) {
        List<Merchant> merchants = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            merchants.add(generate());
        }
        return merchants;
    }
}
