package com.wf.adapter.rest;

import com.wf.adapter.rest.dto.PaymentDto;
import com.wf.payments.application.port.PaymentGatewayPort;
import com.wf.payments.domain.model.Payment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentGatewayAdapter implements PaymentGatewayPort {
    @NonNull
    private final WebClient webClient;

    @Override
    public boolean isPaymentValid(Payment payment) {
        return Boolean.TRUE.equals(webClient.post()
                .uri("/payment")
                .body(Mono.just(PaymentDto.builder()
                        .paymentId(payment.getPaymentId().getId().toString())
                        .accountId(payment.getAccountId().getId())
                        .paymentType(payment.getPaymentType().toString())
                        .creditCard(payment.getCreditCard())
                        .amount(payment.getAmount())
                        .build()), PaymentDto.class)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.just(Boolean.TRUE);
                    }
                    return Mono.just(Boolean.FALSE);
                })
                .onErrorResume(error -> Mono.just(Boolean.FALSE))
                .block());
    }
}
