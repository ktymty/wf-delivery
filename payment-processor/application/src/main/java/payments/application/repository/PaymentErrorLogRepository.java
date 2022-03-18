package payments.application.repository;

import payments.domain.entity.PaymentError;

public interface PaymentErrorLogRepository {
    // to store error logs
    void save(PaymentError error);
}
