package com.wf.payments.application.port;

import com.wf.payments.domain.model.Payment;

public interface PaymentGatewayPort {
    // to validate with external payment gateways - visa, paypal, stripe
    boolean isPaymentValid(Payment payment);
}
