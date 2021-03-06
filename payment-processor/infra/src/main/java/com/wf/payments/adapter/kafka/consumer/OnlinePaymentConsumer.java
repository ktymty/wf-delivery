package com.wf.payments.adapter.kafka.consumer;

import com.wf.payments.adapter.kafka.dto.OnlinePaymentDto;
import com.wf.payments.adapter.kafka.exception.OnlinePaymentException;
import com.wf.payments.application.usecase.RegisterPaymentUseCase;
import com.wf.payments.domain.model.Payment;
import com.wf.payments.domain.vo.AccountId;
import com.wf.payments.domain.vo.PaymentId;
import com.wf.payments.domain.vo.PaymentType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Consumer;

@Component("onlinePayment")
@Slf4j
@RequiredArgsConstructor
public class OnlinePaymentConsumer implements Consumer<OnlinePaymentDto> {

    @NonNull
    private final RegisterPaymentUseCase useCase;

    @Override
    public void accept(OnlinePaymentDto onlinePaymentDto) {
        if (PaymentType.ONLINE != PaymentType.of(onlinePaymentDto.getPaymentType())) {
            throw new OnlinePaymentException(onlinePaymentDto.getPaymentId(), onlinePaymentDto.getPaymentType());
        }
        useCase.validateAndRegisterOnlinePayment(Payment.builder()
                .paymentId(new PaymentId(UUID.fromString(onlinePaymentDto.getPaymentId())))
                .accountId(new AccountId(onlinePaymentDto.getAccountId()))
                .paymentType(PaymentType.ONLINE)
                .amount(onlinePaymentDto.getAmount())
                .creditCard(onlinePaymentDto.getCreditCard())
                .createdOn(LocalDateTime.now())
                .build());
    }
}
