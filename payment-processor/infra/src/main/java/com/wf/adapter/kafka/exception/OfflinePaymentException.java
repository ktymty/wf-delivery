package com.wf.adapter.kafka.exception;

import com.wf.payments.domain.exception.BusinessException;

public class OfflinePaymentException extends BusinessException {

    public OfflinePaymentException(String paymentId, String type) {
        super(String.format("Offline expected. Payment: %s is of type %s ", paymentId, type));
    }
}
