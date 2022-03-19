package com.wf.payments.domain.vo;

import lombok.NonNull;

import java.util.stream.Stream;

public enum PaymentType {
    ONLINE, OFFLINE;

    public static PaymentType of(@NonNull String type) {
        return Stream.of(values())
                .filter(paymentType -> paymentType.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
