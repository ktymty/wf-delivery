package com.wf.adapter.kafka.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OfflinePaymentDto {
    @NotNull
    @JsonProperty("payment_id")
    private String paymentId;

    @NotNull
    @JsonProperty("account_id")
    private Integer accountId;

    @NotNull
    @JsonProperty("payment_type")
    private String paymentType;

    @NotNull
    private BigDecimal amount;
    private Integer delay;
}
