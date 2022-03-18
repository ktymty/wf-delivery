package payments.application.client;

import payments.domain.entities.Payment;

public interface PaymentGatewayClient {
    // to validate with payment gateways - visa, paypal, stripe
    boolean isPaymentValid(Payment payment);
}
