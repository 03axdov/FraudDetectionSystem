package org.frauddetection.models.domain;

import java.time.Instant;

public record Account(
    String accountId,
    String customerId,
    String accountType,
    String country,
    Instant createdAt,
    boolean active
) {}