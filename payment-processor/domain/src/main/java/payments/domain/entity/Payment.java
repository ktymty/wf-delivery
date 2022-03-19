package payments.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import payments.domain.model.AccountId;
import payments.domain.model.PaymentId;
import payments.domain.model.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class Payment {

    @NonNull
    private AccountId accountId;

    @NonNull
    private PaymentId paymentId;

    @NonNull
    private PaymentType paymentType;

    private String creditCard;

    @NonNull
    private BigDecimal amount;

    private LocalDateTime createdOn;
}
