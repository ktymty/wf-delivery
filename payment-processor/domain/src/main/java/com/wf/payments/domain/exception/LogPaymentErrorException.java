package com.wf.payments.domain.exception;

import com.wf.payments.domain.vo.PaymentId;

public class LogPaymentErrorException extends RuntimeException {
    public LogPaymentErrorException(PaymentId paymentId, Throwable throwable) {
        super(String.format("Log error with Payment: %s", paymentId.getId()), throwable);
    }
}
