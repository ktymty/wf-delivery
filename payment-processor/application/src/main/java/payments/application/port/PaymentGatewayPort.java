package payments.application.port;

import payments.domain.model.Payment;

public interface PaymentGatewayPort {
    // to validate with external payment gateways - visa, paypal, stripe
    boolean isPaymentValid(Payment payment);
}
