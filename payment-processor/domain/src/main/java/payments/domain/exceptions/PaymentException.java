package payments.domain.exceptions;

import payments.domain.entities.PaymentError;

public abstract class PaymentException extends BusinessException {

    protected PaymentException(String message) {
        super(message);
    }

    public abstract PaymentError toError();
}
