package com.wf.payments.domain.exception;

import lombok.NonNull;
import com.wf.payments.domain.model.PaymentError;
import com.wf.payments.domain.vo.ErrorType;
import com.wf.payments.domain.vo.PaymentId;

public class InvalidPaymentException extends PaymentException {

    private final PaymentId paymentId;

    public InvalidPaymentException(@NonNull PaymentId paymentId) {
        super(String.format("Invalid payment %s", paymentId.getId()));
        this.paymentId = paymentId;
    }

    @Override
    public PaymentError toError() {
        return PaymentError.builder()
            .paymentId(paymentId)
            .error(ErrorType.NETWORK)
            .description("Invalid payment for Payment Gateway.")
            .build();
    }
}
