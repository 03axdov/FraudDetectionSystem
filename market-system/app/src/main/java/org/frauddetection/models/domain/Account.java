package org.frauddetection.models.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

public class Account {
    private final String accountId;
    private final String customerId;
    private final String accountType;
    private final String country;
    private BigDecimal balance;
    private final Instant createdAt;
    private boolean active;

    public Account(
        String accountId,
        String customerId,
        String accountType,
        String country,
        BigDecimal balance,
        Instant createdAt,
        boolean active
    ) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountType = accountType;
        this.country = country;
        this.balance = balance;
        this.createdAt = createdAt;
        this.active = active;
    }

    public String accountId() {
        return accountId;
    }

    public String customerId() {
        return customerId;
    }

    public String accountType() {
        return accountType;
    }

    public String country() {
        return country;
    }

    public BigDecimal balance() {
        return balance;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean active() {
        return active;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public void deactivate() {
        this.active = false;
    }

    public Map<String, Object> toMap() {
        return Map.of(
            "accountId", accountId,
            "customerId", customerId,
            "accountType", accountType,
            "country", country,
            "balance", balance,
            "createdAt", ZonedDateTime.ofInstant(createdAt, ZoneOffset.UTC),
            "active", active
        );
    }
}