package payments.application.repositories;

import payments.domain.entities.PaymentError;

public interface PaymentErrorLogRepository {
    // to store error logs
    void save(PaymentError error);
}
