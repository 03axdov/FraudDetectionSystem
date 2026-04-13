package org.frauddetection.models.domain;

import java.time.Instant;

import org.frauddetection.models.enums.BehaviorProfile;

public record Customer(
    String customerId,
    String name,
    String riskLevel,
    Instant createdAt,
    BehaviorProfile behaviorProfile
) {}
