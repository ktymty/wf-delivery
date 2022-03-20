package com.wf.adapter.rest;

import com.wf.payments.domain.model.Payment;
import com.wf.payments.domain.vo.AccountId;
import com.wf.payments.domain.vo.PaymentId;
import io.netty.channel.ChannelOption;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.wf.payments.domain.vo.PaymentType.OFFLINE;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Payment Gateway Adapter Test")
class PaymentGatewayAdapterTest {

    private ClientAndServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = ClientAndServer.startClientAndServer(9999);
    }

    @AfterEach
    void tearDown() {
        mockServer.stop();
    }

    private WebClient createWebClient() {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(3))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:9999")
                .build();
    }

    @Test
    @DisplayName("should validate payment and respond with valid if HttpStatus is 2xx.")
    void should_validate_payment_and_return_HttpStatus200_with_valid_true() {
        // given
        WebClient webClient = createWebClient();
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
        PaymentGatewayAdapter serviceUnderTest = new PaymentGatewayAdapter(webClient);
        mockServer.when(HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/payment")).
                respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withDelay(TimeUnit.MILLISECONDS, 1000));

        // when
        boolean valid = serviceUnderTest.isPaymentValid(payment);

        // then
        assertTrue(valid);
    }

    @Test
    @DisplayName("should validate payment and respond with invalid if HttpStatus is 5xx.")
    void should_validate_payment_and_return_HttpStatus200_with_valid_false() {
        // given
        WebClient webClient = createWebClient();
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
        PaymentGatewayAdapter serviceUnderTest = new PaymentGatewayAdapter(webClient);
        mockServer.when(HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/payment")).
                respond(HttpResponse.response()
                        .withStatusCode(500)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withDelay(TimeUnit.MILLISECONDS, 1000));

        // when
        boolean valid = serviceUnderTest.isPaymentValid(payment);

        // then
        assertFalse(valid);
    }

    @Test
    @DisplayName("should validate payment and respond with invalid even if HttpStatus is 2xx but timeout.")
    void when_validate_payment_if_status_ok_and_timeout_should_return_not_valid() {
        // given
        WebClient webClient = createWebClient();
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
        PaymentGatewayAdapter serviceUnderTest = new PaymentGatewayAdapter(webClient);
        mockServer.when(HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/payment")).
                respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withDelay(TimeUnit.MILLISECONDS, 5000));

        // when
        boolean valid = serviceUnderTest.isPaymentValid(payment);

        // then
        assertFalse(valid);
    }
}