package payments.application.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import payments.application.client.PaymentGatewayClient;
import payments.application.repository.AccountRepository;
import payments.application.repository.PaymentErrorLogRepository;
import payments.application.repository.PaymentRepository;
import payments.domain.entity.Payment;
import payments.domain.vo.AccountId;
import payments.domain.vo.PaymentId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static payments.domain.vo.PaymentType.OFFLINE;
import static payments.domain.vo.PaymentType.ONLINE;

@DisplayName("Register Payment Service")
class RegisterPaymentServiceTest {


    private PaymentRepository mockPaymentRepository = mock(PaymentRepository.class);
    private AccountRepository mockAccountRepository = mock(AccountRepository.class);
    private PaymentGatewayClient mockPaymentGatewayClient = mock(PaymentGatewayClient.class);
    private PaymentErrorLogRepository mockPaymentErrorLogRepository = mock(PaymentErrorLogRepository.class);


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

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
        RegisterPaymentService serviceUnderTest = new RegisterPaymentService(mockPaymentRepository, mockAccountRepository,
                mockPaymentGatewayClient, mockPaymentErrorLogRepository);

        when(mockPaymentRepository.exists(paymentId)).thenReturn(true);

        // when
        serviceUnderTest.validateAndRegisterOnlinePayment(payment);

        // THEN
        verify(mockAccountRepository, never()).update(any());
        verify(mockPaymentRepository, never()).save(payment);
        verify(mockPaymentErrorLogRepository, never()).save(any());
    }
}