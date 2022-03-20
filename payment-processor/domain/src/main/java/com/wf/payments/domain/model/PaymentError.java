package com.wf.payments.domain.model;

import com.wf.payments.domain.vo.ErrorType;
import com.wf.payments.domain.vo.PaymentId;
import lombok.Getter;
import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PaymentError {

    @NonNull
    private PaymentId paymentId;

    @NonNull
    private ErrorType error;

    @NonNull
    private String description;
}
