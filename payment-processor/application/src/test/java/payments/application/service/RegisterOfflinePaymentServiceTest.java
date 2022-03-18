package payments.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import payments.application.client.PaymentGatewayClient;
import payments.application.repository.AccountRepository;
import payments.application.repository.PaymentErrorLogRepository;
import payments.application.repository.PaymentRepository;
import payments.domain.entity.Account;
import payments.domain.entity.Payment;
import payments.domain.entity.PaymentError;
import payments.domain.exception.AccountNotFoundException;
import payments.domain.vo.AccountId;
import payments.domain.vo.PaymentId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static payments.domain.vo.ErrorType.OTHER;
import static payments.domain.vo.PaymentType.OFFLINE;

@DisplayName("Register Offline Payment Service Test")
class RegisterOfflinePaymentServiceTest {

    private final PaymentRepository mockPaymentRepository = mock(PaymentRepository.class);
    private final AccountRepository mockAccountRepository = mock(AccountRepository.class);
    private final PaymentGatewayClient mockPaymentGatewayClient = mock(PaymentGatewayClient.class);
    private final PaymentErrorLogRepository mockPaymentErrorLogRepository = mock(PaymentErrorLogRepository.class);

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
        RegisterPaymentService serviceUnderTest = new RegisterPaymentService(mockPaymentRepository, mockAccountRepository,
                mockPaymentGatewayClient, mockPaymentErrorLogRepository);

        when(mockPaymentRepository.exists(paymentId)).thenReturn(true);

        // when
        serviceUnderTest.registerOfflinePayment(payment);

        // then
        verify(mockAccountRepository, never()).update(any());
        verify(mockPaymentRepository, never()).save(payment);
        verify(mockPaymentErrorLogRepository, never()).save(any());
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
        RegisterPaymentService serviceUnderTest = new RegisterPaymentService(mockPaymentRepository, mockAccountRepository,
                mockPaymentGatewayClient, mockPaymentErrorLogRepository);

        when(mockPaymentRepository.exists(paymentId)).thenReturn(false);
        when(mockAccountRepository.fetch(accountId)).thenReturn(null);

        // when
        serviceUnderTest.registerOfflinePayment(payment);

        // then
        verify(mockAccountRepository, never()).update(any());
        verify(mockPaymentRepository, never()).save(payment);
        verify(mockPaymentErrorLogRepository).save(new AccountNotFoundException(paymentId, accountId).toError());
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
        RegisterPaymentService serviceUnderTest = new RegisterPaymentService(mockPaymentRepository, mockAccountRepository,
                mockPaymentGatewayClient, mockPaymentErrorLogRepository);

        when(mockPaymentRepository.exists(paymentId)).thenReturn(false);
        when(mockAccountRepository.fetch(accountId)).thenThrow(new RuntimeException("Some Error"));

        // when
        serviceUnderTest.registerOfflinePayment(payment);

        // then
        verify(mockAccountRepository, never()).update(any());
        verify(mockPaymentRepository, never()).save(payment);
        verify(mockPaymentErrorLogRepository).save(PaymentError.builder()
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
        RegisterPaymentService serviceUnderTest = new RegisterPaymentService(mockPaymentRepository, mockAccountRepository,
                mockPaymentGatewayClient, mockPaymentErrorLogRepository);

        when(mockPaymentRepository.exists(paymentId)).thenReturn(false);
        when(mockAccountRepository.fetch(accountId)).thenReturn(account);

        // when
        serviceUnderTest.registerOfflinePayment(payment);

        // then
        assertEquals(paymentCreatedOn, account.getLastPaymentDate());
        verify(mockAccountRepository).update(account);
        verify(mockPaymentRepository).save(payment);
        verify(mockPaymentErrorLogRepository, never()).save(any());
    }
}