package payments.application.port;

import payments.domain.model.PaymentError;

public interface PaymentErrorLogPort {
    // to store error logs
    void savePaymentError(PaymentError error);
}
