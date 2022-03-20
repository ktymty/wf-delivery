package com.wf.payments.adapter.rest.client;

import com.wf.payments.adapter.rest.dto.PaymentErrorDto;
import com.wf.payments.application.port.PaymentErrorLogPort;
import com.wf.payments.domain.exception.LogPaymentErrorException;
import com.wf.payments.domain.model.PaymentError;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentErrorLogAdapter implements PaymentErrorLogPort {
    @NonNull
    private final WebClient webClient;

    @Override
    public void sendErrorToLogSystem(@NonNull PaymentError error) {
        webClient.post()
                .uri("/log")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(PaymentErrorDto.builder()
                        .paymentId(error.getPaymentId().getId().toString())
                        .errorType(error.getError().toString())
                        .errorDescription(error.getDescription())
                        .build()), PaymentErrorDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(Retry.backoff(3, Duration.ofMillis(200)))
                .onErrorMap(ex -> new LogPaymentErrorException(error.getPaymentId(), ex))
                .block();
    }
}
