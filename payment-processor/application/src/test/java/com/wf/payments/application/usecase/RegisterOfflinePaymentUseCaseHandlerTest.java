package com.wf.payments.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.wf.payments.application.port.PaymentGatewayPort;
import com.wf.payments.application.port.AccountPort;
import com.wf.payments.application.port.PaymentErrorLogPort;
import com.wf.payments.application.port.PaymentPort;
import com.wf.payments.domain.model.Account;
import com.wf.payments.domain.model.Payment;
import com.wf.payments.domain.model.PaymentError;
import com.wf.payments.domain.exception.AccountNotFoundException;
import com.wf.payments.domain.vo.AccountId;
import com.wf.payments.domain.vo.PaymentId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.wf.payments.domain.vo.ErrorType.OTHER;
import static com.wf.payments.domain.vo.PaymentType.OFFLINE;

@DisplayName("Register Offline Payment Use Case Handler Test")
class RegisterOfflinePaymentUseCaseHandlerTest {

    private final PaymentPort mockPaymentPort = mock(PaymentPort.class);
    private final AccountPort mockAccountPort = mock(AccountPort.class);
    private final PaymentGatewayPort mockPaymentGatewayPort = mock(PaymentGatewayPort.class);
    private final PaymentErrorLogPort mockPaymentErrorLogPort = mock(PaymentErrorLogPort.class);

    @Test
    @DisplayName("should register offline payment when paymentId exists.")
    void register_offline_payment() {
        // given
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        AccountId accountId = new AccountId(1);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(OFFLINE)
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        RegisterPaymentUseCaseHandler serviceUnderTest = new RegisterPaymentUseCaseHandler(mockAccountPort, mockPaymentPort,
                mockPaymentGatewayPort, mockPaymentErrorLogPort);

        when(mockPaymentPort.exists(paymentId)).thenReturn(true);

        // when
        serviceUnderTest.registerOfflinePayment(payment);

        // then
        verify(mockAccountPort, never()).updateAccount(any());
        verify(mockPaymentPort, never()).savePayment(payment);
        verify(mockPaymentErrorLogPort, never()).sendErrorToLogSystem(any());
    }

    @Test
    @DisplayName("should log error when not existing offline payment and invalid account")
    void should_log_error_when_offline_payment_not_exists_and_account_invalid() {
        // given
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        AccountId accountId = new AccountId(1);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(OFFLINE)
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        RegisterPaymentUseCaseHandler serviceUnderTest = new RegisterPaymentUseCaseHandler(mockAccountPort, mockPaymentPort,
                mockPaymentGatewayPort, mockPaymentErrorLogPort);

        when(mockPaymentPort.exists(paymentId)).thenReturn(false);
        when(mockAccountPort.findByAccountId(accountId)).thenReturn(null);

        // when
        serviceUnderTest.registerOfflinePayment(payment);

        // then
        verify(mockAccountPort, never()).updateAccount(any());
        verify(mockPaymentPort, never()).savePayment(payment);
        verify(mockPaymentErrorLogPort).sendErrorToLogSystem(new AccountNotFoundException(paymentId, accountId).toError());
    }

    @Test
    @DisplayName("should not register not existing offline payment and log error")
    void should_not_register_not_exist_offline_payment_and_log_error() {
        // given
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        AccountId accountId = new AccountId(1);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(OFFLINE)
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        RegisterPaymentUseCaseHandler serviceUnderTest = new RegisterPaymentUseCaseHandler(mockAccountPort, mockPaymentPort,
                mockPaymentGatewayPort, mockPaymentErrorLogPort);

        when(mockPaymentPort.exists(paymentId)).thenReturn(false);
        when(mockAccountPort.findByAccountId(accountId)).thenThrow(new RuntimeException("Some Error"));

        // when
        serviceUnderTest.registerOfflinePayment(payment);

        // then
        verify(mockAccountPort, never()).updateAccount(any());
        verify(mockPaymentPort, never()).savePayment(payment);
        verify(mockPaymentErrorLogPort).sendErrorToLogSystem(PaymentError.builder()
                .paymentId(paymentId)
                .error(OTHER)
                .description("Some Error")
                .build());
    }

    @Test
    @DisplayName("should register not existing offline payment when account exists and update account.")
    void should_register_not_existing_offline_payment_when_account_exists_and_update_account() {
        // given
        var paymentCreatedOn = LocalDateTime.of(2022, 3, 1, 10, 10, 10);
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        AccountId accountId = new AccountId(1);
        Payment payment = Payment.builder()
                .paymentId(paymentId)
                .accountId(accountId)
                .paymentType(OFFLINE)
                .amount(BigDecimal.TEN)
                .createdOn(paymentCreatedOn)
                .build();
        Account account = Account.builder()
                .accountId(accountId)
                .name("wf")
                .email("mail@wf.com")
                .birthdate(LocalDate.of(2000, 1, 1))
                .build();
        RegisterPaymentUseCaseHandler serviceUnderTest = new RegisterPaymentUseCaseHandler(mockAccountPort, mockPaymentPort,
                mockPaymentGatewayPort, mockPaymentErrorLogPort);

        when(mockPaymentPort.exists(paymentId)).thenReturn(false);
        when(mockAccountPort.findByAccountId(accountId)).thenReturn(account);

        // when
        serviceUnderTest.registerOfflinePayment(payment);

        // then
        assertEquals(paymentCreatedOn, account.getLastPaymentDate());
        verify(mockAccountPort).updateAccount(account);
        verify(mockPaymentPort).savePayment(payment);
        verify(mockPaymentErrorLogPort, never()).sendErrorToLogSystem(any());
    }
}