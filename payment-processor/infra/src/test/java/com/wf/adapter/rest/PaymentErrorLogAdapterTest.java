package com.wf.adapter.rest;

import com.wf.payments.domain.exception.LogPaymentErrorException;
import com.wf.payments.domain.model.PaymentError;
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

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.wf.payments.domain.vo.ErrorType.NETWORK;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Payment Error Log Adapter Test")
class PaymentErrorLogAdapterTest {

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

    private String resquestBody(PaymentError error) {
        return String.format("{ \"payment_id\": \"%s\", \"error_type\": \"%s\", "
                        + "\"error_description\": \"%s\"}", error.getPaymentId().getId().toString(),
                error.getError(), error.getDescription());
    }

    @Test
    @DisplayName("should ")
    void when_log_if_service_status_error_should_throw_exception() {
        // given
        WebClient webClient = createWebClient();
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        PaymentError paymentError = PaymentError.builder()
                .paymentId(paymentId)
                .error(NETWORK)
                .description("Error description")
                .build();

        PaymentErrorLogAdapter serviceUnderTest = new PaymentErrorLogAdapter(webClient);

        // when
        mockServer.when(HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/log")
                        .withBody(resquestBody(paymentError))).
                respond(HttpResponse.response()
                        .withStatusCode(504)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withDelay(TimeUnit.MILLISECONDS, 1000));

        // then
        assertThrows(LogPaymentErrorException.class, () -> serviceUnderTest.savePaymentError(paymentError));
    }
}