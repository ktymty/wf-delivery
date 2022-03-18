package payments.domain.entities;

import lombok.Getter;
import lombok.Builder;
import lombok.NonNull;
import lombok.ToString;
import payments.domain.vo.AccountId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class Account {

    @NonNull
    private AccountId accountId;

    private String name;

    @NonNull
    private String email;

    private LocalDate birthdate;

    private LocalDateTime lastPaymentDate;

    public void addPayment(@NonNull Payment payment) {
        this.lastPaymentDate = payment.getCreatedOn();
    }
}
