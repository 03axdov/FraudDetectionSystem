package org.frauddetection.models.domain;

import java.util.Map;

public record IpAddress(
    String ipAddress
) {
    public Map<String, Object> toMap() {
        return Map.of(
            "ipAddress", ipAddress
        );
    }
}