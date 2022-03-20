package com.wf.payments.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentDto {
    @JsonProperty("payment_id")
    String paymentId;
    @JsonProperty("account_id")
    Integer accountId;
    @JsonProperty("payment_type")
    String paymentType;
    @JsonProperty("credit_card")
    String creditCard;
    BigDecimal amount;
}
