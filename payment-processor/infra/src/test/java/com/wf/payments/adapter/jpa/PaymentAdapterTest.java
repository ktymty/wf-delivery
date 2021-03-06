package com.wf.payments.adapter.jpa;

import com.wf.payments.adapter.jpa.repository.PaymentRepository;
import com.wf.payments.adapter.jpa.entity.PaymentEntity;
import com.wf.payments.adapter.jpa.service.PaymentAdapter;
import com.wf.payments.domain.model.Payment;
import com.wf.payments.domain.vo.AccountId;
import com.wf.payments.domain.vo.PaymentId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.wf.payments.domain.vo.PaymentType.OFFLINE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Payment Adapter Test")
@ExtendWith(MockitoExtension.class)
class PaymentAdapterTest {

    @InjectMocks
    private PaymentAdapter paymentAdapter;
    @Mock
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("should call payment repository once to check if paymentId exists")
    void should_call_once_payment_repository_existsById() {
        //given
        PaymentId paymentId = new PaymentId(UUID.randomUUID());

        //when
        paymentAdapter.exists(paymentId);

        //then
        verify(paymentRepository, times(1)).existsById(anyString());

    }

    @Test
    @DisplayName("should call payment repository once to save given payment")
    void should_save_given_payment_by_calling_payment_repository_once() {
        //given
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
        //when
        paymentAdapter.savePayment(payment);

        //then
        verify(paymentRepository, times(1)).save(any(PaymentEntity.class));
    }
}