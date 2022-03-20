package com.wf.payments.adapter.kafka.exception;

import com.wf.payments.domain.exception.BusinessException;

public class OnlinePaymentException extends BusinessException {
    public OnlinePaymentException(String paymentId, String type) {
        super(String.format("Online expected. Payment: %s is of type %s ", paymentId, type));
    }
}
