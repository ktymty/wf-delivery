package com.wf.payments.application.port;

import com.wf.payments.domain.model.PaymentError;

public interface PaymentErrorLogPort {
    // to store error logs
    void savePaymentError(PaymentError error);
}
