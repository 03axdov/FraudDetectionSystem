package org.frauddetection.models.events;

import java.math.BigDecimal;
import java.time.Instant;

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
) {}
