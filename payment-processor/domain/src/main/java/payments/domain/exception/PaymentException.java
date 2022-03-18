package payments.domain.exception;

import payments.domain.entity.PaymentError;

public abstract class PaymentException extends BusinessException {

    protected PaymentException(String message) {
        super(message);
    }

    public abstract PaymentError toError();
}
