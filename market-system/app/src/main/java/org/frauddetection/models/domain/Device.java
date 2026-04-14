package org.frauddetection.models.domain;

import java.util.Map;

public record Device(
    String deviceId,
    String deviceType
) {
    public Map<String, Object> toMap() {
        return Map.of(
            "deviceId", deviceId,
            "deviceType", deviceType
        );
    }
}