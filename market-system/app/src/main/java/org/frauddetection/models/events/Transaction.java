package org.frauddetection.models.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

import org.frauddetection.models.enums.TransactionStatus;
import org.frauddetection.models.enums.TransactionType;

public record Transaction(
    String transactionId,
    Instant timestamp,
    String fromAccountId,
    String toAccountId,
    BigDecimal amount,
    String currency,
    TransactionType transactionType,
    TransactionStatus status,
    String channel,
    String deviceId,
    String ipAddress,
    String merchantId,
    String country,
    String city
) {
    public Map<String, Object> toMap() {
        return Map.ofEntries(
            Map.entry("transactionId", transactionId),
            Map.entry("timestamp", ZonedDateTime.ofInstant(timestamp, ZoneOffset.UTC)),
            Map.entry("fromAccountId", fromAccountId),
            Map.entry("toAccountId", toAccountId),
            Map.entry("amount", amount),
            Map.entry("currency", currency),
            Map.entry("transactionType", transactionType.name()),
            Map.entry("status", status.name()),
            Map.entry("channel", channel),
            Map.entry("deviceId", deviceId),
            Map.entry("ipAddress", ipAddress),
            Map.entry("merchantId", merchantId),
            Map.entry("country", country),
            Map.entry("city", city)
        );
    }
}
