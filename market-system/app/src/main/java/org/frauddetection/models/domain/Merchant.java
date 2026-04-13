package org.frauddetection.models.domain;

public record Merchant(
    String merchantId,
    String category,
    String country
) {}
