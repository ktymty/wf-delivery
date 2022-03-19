package payments.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import payments.domain.vo.AccountId;
import payments.domain.vo.PaymentId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static payments.domain.vo.PaymentType.OFFLINE;

@DisplayName("Account")
class AccountTest {

    @Test
    @DisplayName("should update last payment date when payment is added to account.")
    void should_update_last_payment_date_when_payment_added_to_account() {

        // given
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        var paymentId = new PaymentId(UUID.randomUUID());
        var accountId = new AccountId(1);
        var payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(OFFLINE)
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        var accountUnderTest = Account.builder()
                .accountId(accountId)
                .name("wf")
                .email("mail@wf.com")
                .birthdate(LocalDate.of(2000, 1, 1))
                .build();

        // when
        accountUnderTest.addPayment(payment);

        // then
        assertEquals(paymentCreatedOn, accountUnderTest.getLastPaymentDate());
    }
}