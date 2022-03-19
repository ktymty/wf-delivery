package payments.domain.exception;

import lombok.NonNull;
import payments.domain.entity.PaymentError;
import payments.domain.model.ErrorType;
import payments.domain.model.PaymentId;

public class InvalidPaymentException extends PaymentException {

    private final PaymentId paymentId;

    public InvalidPaymentException(@NonNull PaymentId paymentId) {
        super(String.format("Invalid payment %s", paymentId.getId()));
        this.paymentId = paymentId;
    }

    @Override
    public PaymentError toError() {
        return PaymentError.builder()
            .paymentId(paymentId)
            .error(ErrorType.NETWORK)
            .description("Invalid payment for Payment Gateway.")
            .build();
    }
}
