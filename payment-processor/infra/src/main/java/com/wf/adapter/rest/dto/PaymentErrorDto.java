package com.wf.adapter.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentErrorDto {
    @JsonProperty("payment_id")
    String paymentId;
    @JsonProperty("error_type")
    String errorType;
    @JsonProperty("error_description")
    String errorDescription;
}
