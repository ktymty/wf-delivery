package payments.application.port;

import payments.domain.entity.PaymentError;

public interface PaymentErrorLogPort {
    // to store error logs
    void save(PaymentError error);
}
