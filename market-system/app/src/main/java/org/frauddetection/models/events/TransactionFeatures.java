package org.frauddetection.models.events;

import java.math.BigDecimal;

public record TransactionFeatures(
    String transactionId,
    int outgoingCountLast10Min,
    int incomingCountLast10Min,
    BigDecimal avgOutgoingAmount30d,
    boolean deviceSharedByMultipleAccounts,
    boolean ipSharedByMultipleAccounts,
    boolean unusualCountry,
    int accountAgeDays
) {}
