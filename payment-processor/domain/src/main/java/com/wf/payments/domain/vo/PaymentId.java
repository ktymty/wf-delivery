package com.wf.payments.domain.vo;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class PaymentId {
    @NonNull
    UUID id;
}
