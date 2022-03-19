package com.wf.payments.domain.model;

import com.wf.payments.domain.vo.AccountId;
import com.wf.payments.domain.vo.PaymentId;
import com.wf.payments.domain.vo.PaymentType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class Payment {

    @NonNull
    private AccountId accountId;

    @NonNull
    private PaymentId paymentId;

    @NonNull
    private PaymentType paymentType;

    private String creditCard;

    @NonNull
    private BigDecimal amount;

    private LocalDateTime createdOn;
}
