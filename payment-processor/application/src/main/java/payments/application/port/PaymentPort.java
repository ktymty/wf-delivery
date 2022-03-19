package payments.application.port;

import payments.domain.model.Payment;
import payments.domain.vo.PaymentId;

public interface PaymentPort {
    boolean exists(PaymentId paymentId);

    void savePayment(Payment payment);
}
