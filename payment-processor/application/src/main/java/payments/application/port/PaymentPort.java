package payments.application.port;

import payments.domain.entity.Payment;
import payments.domain.model.PaymentId;

public interface PaymentPort {
    boolean exists(PaymentId paymentId);

    void save(Payment payment);
}
