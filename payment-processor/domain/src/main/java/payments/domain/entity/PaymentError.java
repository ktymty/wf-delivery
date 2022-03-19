package payments.domain.entity;

import lombok.Getter;
import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import payments.domain.model.ErrorType;
import payments.domain.model.PaymentId;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PaymentError {

    @NonNull
    private PaymentId paymentId;

    @NonNull
    private ErrorType error;

    @NonNull
    private String description;
}
