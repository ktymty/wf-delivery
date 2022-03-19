package com.wf.adapter.kafka.consumer;

import com.wf.adapter.kafka.dto.OfflinePaymentDto;
import com.wf.adapter.kafka.exception.OfflinePaymentException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.wf.payments.application.usecase.RegisterPaymentUseCase;
import com.wf.payments.domain.model.Payment;
import com.wf.payments.domain.vo.AccountId;
import com.wf.payments.domain.vo.PaymentId;
import com.wf.payments.domain.vo.PaymentType;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

@Component("offlinePayment")
@Slf4j
@RequiredArgsConstructor
public class OfflinePaymentConsumer implements Consumer<OfflinePaymentDto> {

    @NonNull
    private final RegisterPaymentUseCase useCase;

    // best practice for applications is to pass a Clock into any method that requires the current instant. Also helps in unit testing.
    @NonNull
    private final Clock clock;

    @Override
    public void accept(OfflinePaymentDto offlinePaymentDto) {
        if (PaymentType.OFFLINE != PaymentType.of(offlinePaymentDto.getPaymentType())) {
            throw new OfflinePaymentException(offlinePaymentDto.getPaymentId(), offlinePaymentDto.getPaymentType());
        }
        useCase.registerOfflinePayment(Payment.builder()
                .paymentId(new PaymentId(UUID.fromString(offlinePaymentDto.getPaymentId())))
                .accountId(new AccountId(offlinePaymentDto.getAccountId()))
                .paymentType(PaymentType.OFFLINE)
                .amount(offlinePaymentDto.getAmount())
                .createdOn(LocalDateTime.now(clock))
                .build());
    }
}
