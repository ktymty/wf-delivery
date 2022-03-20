package com.wf.payments.application.port;

import com.wf.payments.domain.model.PaymentError;

public interface PaymentErrorLogPort {
    // send to Log system via post rest call
    void sendErrorToLogSystem(PaymentError error);
}
