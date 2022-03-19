package payments.application.port;

import payments.domain.entity.Payment;

public interface PaymentGatewayPort {
    // to validate with payment gateways - visa, paypal, stripe
    boolean isPaymentValid(Payment payment);
}
