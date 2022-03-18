package payments.application.repository;

import payments.domain.entity.Payment;
import payments.domain.vo.PaymentId;

public interface PaymentRepository {
    boolean exists(PaymentId paymentId);

    void save(Payment payment);
}
