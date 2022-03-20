package com.wf.payments.adapter.jpa.service;

import com.wf.payments.adapter.jpa.repository.PaymentRepository;
import com.wf.payments.adapter.jpa.entity.PaymentEntity;
import com.wf.payments.application.port.PaymentPort;
import com.wf.payments.domain.model.Payment;
import com.wf.payments.domain.vo.PaymentId;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentPort {
    @NonNull
    private final PaymentRepository paymentRepository;

    @Override
    public boolean exists(PaymentId paymentId) {
        return paymentRepository.existsById(paymentId.getId().toString());
    }

    @Override
    public void savePayment(Payment payment) {
        paymentRepository.save(toPaymentEntity(payment));
    }

    private PaymentEntity toPaymentEntity(Payment payment) {
        return PaymentEntity.builder()
                .paymentId(payment.getPaymentId().getId().toString())
                .accountId(payment.getAccountId().getId())
                .paymentType(payment.getPaymentType().toString())
                .creditCard(payment.getCreditCard())
                .amount(payment.getAmount())
                .createdOn(payment.getCreatedOn())
                .build();
    }
}
