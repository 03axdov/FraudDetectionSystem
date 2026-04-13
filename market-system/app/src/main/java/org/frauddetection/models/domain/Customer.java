package org.frauddetection.models.domain;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

import org.frauddetection.models.enums.BehaviorProfile;

public record Customer(
    String customerId,
    String name,
    String riskLevel,
    Instant createdAt,
    BehaviorProfile behaviorProfile
) {
    public Map<String, Object> toMap() {
        return Map.of(
            "customerId", customerId,
            "name", name,
            "riskLevel", riskLevel,
            "createdAt", ZonedDateTime.ofInstant(createdAt, ZoneOffset.UTC),
            "behaviorProfile", behaviorProfile.name()
        );
    }
}
