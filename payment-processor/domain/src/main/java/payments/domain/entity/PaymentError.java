package payments.domain.entity;

import lombok.Getter;
import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import payments.domain.vo.ErrorType;
import payments.domain.vo.PaymentId;

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
