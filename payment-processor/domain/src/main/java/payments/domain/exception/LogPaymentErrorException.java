package payments.domain.exception;

import payments.domain.model.PaymentId;

public class LogPaymentErrorException extends RuntimeException {

    public LogPaymentErrorException(PaymentId paymentId, Throwable throwable) {
        super(String.format("Log error with Payment: %s", paymentId.getId()), throwable);
    }
}
