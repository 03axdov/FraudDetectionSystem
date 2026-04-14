package org.frauddetection.simulator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.frauddetection.models.domain.Account;
import org.frauddetection.models.domain.Customer;
import org.frauddetection.repositories.AccountRepository;

public class AccountGenerator {
    private static final List<String> ACCOUNT_TYPES = List.of("CHECKING", "SAVINGS", "BUSINESS", "WALLET");
    private static final List<String> COUNTRIES = List.of("DE", "NL", "FR", "ES", "IT", "US", "GB", "SE");

    private final Random random;
    private final AccountRepository accountRepository;

    public AccountGenerator(AccountRepository accountRepository, Random random) {
        this.accountRepository = accountRepository;
        this.random = random;
    }

    private BigDecimal biasedRandomAmount(Long maxAmount) {
        double u = random.nextDouble(1);
        double biased = Math.pow(u, 3);

        return BigDecimal.valueOf(maxAmount * biased);
    }

    public Account generate(Customer customer) {
        Instant createdAt = Instant.now();

        Account account = new Account(
            "acct-" + UUID.randomUUID(),
            customer.customerId(),
            ACCOUNT_TYPES.get(random.nextInt(ACCOUNT_TYPES.size())),
            COUNTRIES.get(random.nextInt(COUNTRIES.size())),
            biasedRandomAmount(10_000_000_000L),
            createdAt,
            random.nextDouble() < 0.95
        );
        accountRepository.save(account);
        return account;
    }
}
