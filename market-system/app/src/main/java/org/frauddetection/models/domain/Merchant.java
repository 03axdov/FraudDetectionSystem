package org.frauddetection.models.domain;

import java.util.Map;

public record Merchant(
    String merchantId,
    String category,
    String country
) {
    public Map<String, Object> toMap() {
        return Map.of(
            "merchantId", merchantId,
            "category", category,
            "country", country
        );
    }
}
