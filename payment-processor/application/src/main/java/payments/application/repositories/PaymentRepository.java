package payments.application.repositories;

import payments.domain.entities.Payment;
import payments.domain.vo.PaymentId;

public interface PaymentRepository {
    boolean exists(PaymentId paymentId);
    void save(Payment payment);
}
