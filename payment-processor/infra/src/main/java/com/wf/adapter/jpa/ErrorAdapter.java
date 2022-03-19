package com.wf.adapter.jpa;

import com.wf.adapter.jpa.entity.ErrorEntity;
import com.wf.adapter.jpa.repository.ErrorRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.wf.payments.application.port.PaymentErrorLogPort;
import com.wf.payments.domain.model.PaymentError;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErrorAdapter implements PaymentErrorLogPort {
    @NonNull
    private final ErrorRepository errorRepository;

    @Override
    public void savePaymentError(PaymentError error) {
        errorRepository.save(toErrorEntity(error));
    }

    private ErrorEntity toErrorEntity(PaymentError paymentError) {
        return ErrorEntity.builder()
                .paymentId(paymentError.getPaymentId().toString())
                .error(paymentError.getError().name())
                .errorDescription(paymentError.getDescription())
                .build();
    }
}
