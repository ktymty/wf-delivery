package payments.domain.exception;

import payments.domain.model.PaymentError;

public abstract class PaymentException extends BusinessException {

    protected PaymentException(String message) {
        super(message);
    }

    public abstract PaymentError toError();
}
