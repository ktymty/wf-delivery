package payments.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import payments.application.port.PaymentGatewayPort;
import payments.application.port.AccountPort;
import payments.application.port.PaymentErrorLogPort;
import payments.application.port.PaymentPort;
import payments.domain.entity.Account;
import payments.domain.entity.Payment;
import payments.domain.entity.PaymentError;
import payments.domain.exception.AccountNotFoundException;
import payments.domain.exception.InvalidPaymentException;
import payments.domain.model.AccountId;
import payments.domain.model.PaymentId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static payments.domain.model.ErrorType.OTHER;
import static payments.domain.model.PaymentType.ONLINE;

@DisplayName("Register Online Payment Use Case Handler Test")
class RegisterOnlinePaymentUseCaseHandlerTest {

    private final PaymentPort mockPaymentPort = mock(PaymentPort.class);
    private final AccountPort mockAccountPort = mock(AccountPort.class);
    private final PaymentGatewayPort mockPaymentGatewayPort = mock(PaymentGatewayPort.class);
    private final PaymentErrorLogPort mockPaymentErrorLogPort = mock(PaymentErrorLogPort.class);

    @Test
    @DisplayName("should validate and register online payment when paymentId exists.")
    void validate_and_register_online_payment() {
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        AccountId accountId = new AccountId(1);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(ONLINE)
                .creditCard("12345")
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        RegisterPaymentUseCaseHandler serviceUnderTest = new RegisterPaymentUseCaseHandler(mockPaymentPort, mockAccountPort,
                mockPaymentGatewayPort, mockPaymentErrorLogPort);

        when(mockPaymentPort.exists(paymentId)).thenReturn(true);

        // when
        serviceUnderTest.validateAndRegisterOnlinePayment(payment);

        // then
        verify(mockAccountPort, never()).update(any());
        verify(mockPaymentPort, never()).save(payment);
        verify(mockPaymentErrorLogPort, never()).save(any());
    }

    @Test
    @DisplayName("should log error when payment is valid but does not exists and account is invalid")
    void should_save_error_log_when_payment_not_exists_but_valid_and_account_is_invalid() {
        // given
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        AccountId accountId = new AccountId(1);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(ONLINE)
                .creditCard("12345")
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        RegisterPaymentUseCaseHandler serviceUnderTest = new RegisterPaymentUseCaseHandler(mockPaymentPort, mockAccountPort,
                mockPaymentGatewayPort, mockPaymentErrorLogPort);

        when(mockPaymentPort.exists(paymentId)).thenReturn(false);
        when(mockPaymentGatewayPort.isPaymentValid(payment)).thenReturn(true);
        when(mockAccountPort.fetch(accountId)).thenReturn(null);

        // when
        serviceUnderTest.validateAndRegisterOnlinePayment(payment);

        // then
        verify(mockAccountPort, never()).update(any());
        verify(mockPaymentPort, never()).save(payment);
        verify(mockPaymentErrorLogPort).save(new AccountNotFoundException(paymentId, accountId).toError());
    }

    @Test
    @DisplayName("should log error when payment does not exist and is invalid from gateway.")
    void should_log_error_when_payment_does_not_exist_and_invalid_from_gateway() {
        // given
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        AccountId accountId = new AccountId(1);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(ONLINE)
                .creditCard("12345")
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        RegisterPaymentUseCaseHandler serviceUnderTest = new RegisterPaymentUseCaseHandler(mockPaymentPort, mockAccountPort,
                mockPaymentGatewayPort, mockPaymentErrorLogPort);

        when(mockPaymentPort.exists(paymentId)).thenReturn(false);
        when(mockPaymentGatewayPort.isPaymentValid(payment)).thenReturn(false);

        // when
        serviceUnderTest.validateAndRegisterOnlinePayment(payment);

        // then
        verify(mockAccountPort, never()).update(any());
        verify(mockPaymentPort, never()).save(payment);
        verify(mockPaymentErrorLogPort).save(new InvalidPaymentException(paymentId).toError());
    }

    @Test
    @DisplayName("should validate and register not existing online payment when payment is valid from gateway and update account.")
    void should_validate_and_register_not_existing_online_payment_when_account_exists_and_update_account() {
        // given
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        AccountId accountId = new AccountId(1);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(ONLINE)
                .creditCard("12345")
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        Account account = Account.builder()
                .accountId(accountId)
                .name("wf")
                .email("mail@wf.com")
                .birthdate(LocalDate.of(2000, 1, 1))
                .build();
        RegisterPaymentUseCaseHandler serviceUnderTest = new RegisterPaymentUseCaseHandler(mockPaymentPort, mockAccountPort,
                mockPaymentGatewayPort, mockPaymentErrorLogPort);

        when(mockPaymentPort.exists(paymentId)).thenReturn(false);
        when(mockPaymentGatewayPort.isPaymentValid(payment)).thenReturn(true);
        when(mockAccountPort.fetch(accountId)).thenReturn(account);

        // when
        serviceUnderTest.validateAndRegisterOnlinePayment(payment);

        // then
        assertEquals(paymentCreatedOn, account.getLastPaymentDate());
        verify(mockAccountPort).update(account);
        verify(mockPaymentPort).save(payment);
        verify(mockPaymentErrorLogPort, never()).save(any());
    }

    @Test
    @DisplayName("should not register not existing online payment and log error")
    void should_not_register_not_exist_online_payment_and_log_error() {
        // given
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        AccountId accountId = new AccountId(1);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(ONLINE)
                .creditCard("12345")
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        RegisterPaymentUseCaseHandler serviceUnderTest = new RegisterPaymentUseCaseHandler(mockPaymentPort, mockAccountPort,
                mockPaymentGatewayPort, mockPaymentErrorLogPort);

        when(mockPaymentPort.exists(paymentId)).thenReturn(false);
        when(mockPaymentGatewayPort.isPaymentValid(payment)).thenReturn(true);
        when(mockAccountPort.fetch(accountId)).thenThrow(new RuntimeException("Some Error"));

        // when
        serviceUnderTest.validateAndRegisterOnlinePayment(payment);

        // then
        verify(mockAccountPort, never()).update(any());
        verify(mockPaymentPort, never()).save(payment);
        verify(mockPaymentErrorLogPort).save(PaymentError.builder()
                .paymentId(paymentId)
                .error(OTHER)
                .description("Some Error")
                .build());
    }

}