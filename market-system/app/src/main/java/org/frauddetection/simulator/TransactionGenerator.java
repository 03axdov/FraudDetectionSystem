package org.frauddetection.simulator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.frauddetection.models.domain.Account;
import org.frauddetection.models.domain.Device;
import org.frauddetection.models.domain.IpAddress;
import org.frauddetection.models.domain.Merchant;
import org.frauddetection.models.enums.TransactionStatus;
import org.frauddetection.models.enums.TransactionType;
import org.frauddetection.models.events.Transaction;

public class TransactionGenerator {
    private static final List<String> CHANNELS = List.of("MOBILE", "WEB", "POS", "ATM", "API");
    private static final List<String> CITIES = List.of(
        "Berlin", "Amsterdam", "Paris", "Madrid", "Milan", "Stockholm", "New York", "London"
    );
    private static final List<String> CURRENCIES = List.of("EUR", "USD", "GBP", "SEK");

    private final Random random;

    public TransactionGenerator() {
        this(new Random());
    }

    public TransactionGenerator(Random random) {
        this.random = random;
    }

    public Transaction generate(
        List<Account> accounts,
        List<Device> devices,
        List<IpAddress> ipAddresses,
        List<Merchant> merchants
    ) {
        if (accounts.size() < 2) {
            throw new IllegalArgumentException("At least two accounts are required to generate a transaction.");
        }
        if (devices.isEmpty() || ipAddresses.isEmpty() || merchants.isEmpty()) {
            throw new IllegalArgumentException("Devices, IP addresses, and merchants must not be empty.");
        }

        Account fromAccount = randomAccount(accounts);
        Account toAccount = randomDifferentAccount(accounts, fromAccount.accountId());
        Merchant merchant = merchants.get(random.nextInt(merchants.size()));

        TransactionType transactionType = randomTransactionType();

        BigDecimal amount = randomAmount(transactionType, fromAccount);

        fromAccount.withdraw(amount);   // Fine for now
        toAccount.deposit(amount);  // Fine for now

        return new Transaction(
            "txn-" + UUID.randomUUID(),
            Instant.now(),
            fromAccount.accountId(),
            toAccount.accountId(),
            amount,
            CURRENCIES.get(random.nextInt(CURRENCIES.size())),
            transactionType,
            randomStatus(),
            CHANNELS.get(random.nextInt(CHANNELS.size())),
            devices.get(random.nextInt(devices.size())).deviceId(),
            ipAddresses.get(random.nextInt(ipAddresses.size())).ipAddress(),
            merchant.merchantId(),
            merchant.country(),
            CITIES.get(random.nextInt(CITIES.size()))
        );
    }

    private Account randomAccount(List<Account> accounts) {
        return accounts.get(random.nextInt(accounts.size()));
    }

    private Account randomDifferentAccount(List<Account> accounts, String excludedAccountId) {
        Account account;
        do {
            account = randomAccount(accounts);
        } while (account.accountId().equals(excludedAccountId));
        return account;
    }

    private TransactionType randomTransactionType() {
        TransactionType[] values = TransactionType.values();
        return values[random.nextInt(values.length)];
    }

    private TransactionStatus randomStatus() {
        double sample = random.nextDouble();
        if (sample < 0.82) {
            return TransactionStatus.COMPLETED;
        }
        if (sample < 0.92) {
            return TransactionStatus.PENDING;
        }
        if (sample < 0.98) {
            return TransactionStatus.FAILED;
        }
        return TransactionStatus.REVERSED;
    }

    private BigDecimal  biasedRandomAmount(BigDecimal maxAmount, double biasFactor) {
        double u = random.nextDouble(1);
        double biased = Math.pow(u, biasFactor);

        return maxAmount.multiply(BigDecimal.valueOf(biased));
    }

    private BigDecimal randomAmount(TransactionType transactionType, Account fromAccount) {
        BigDecimal currentBalance = fromAccount.balance();
        
        BigDecimal amount = switch (transactionType) {
            case PAYMENT -> biasedRandomAmount(currentBalance, 4);
            case TRANSFER -> biasedRandomAmount(currentBalance, 3);
            case WITHDRAWAL -> biasedRandomAmount(currentBalance, 2.5);
            case DEPOSIT -> biasedRandomAmount(currentBalance.multiply(BigDecimal.valueOf(3)), 4);
            case REFUND -> biasedRandomAmount(currentBalance.divide(BigDecimal.valueOf(5.0), 2, RoundingMode.UP), 3);
        };

        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
