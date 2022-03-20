package com.wf.adapter.jpa;

import com.wf.adapter.jpa.entity.ErrorEntity;
import com.wf.adapter.jpa.repository.ErrorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wf.payments.domain.model.PaymentError;
import com.wf.payments.domain.vo.PaymentId;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static com.wf.payments.domain.vo.ErrorType.OTHER;

@DisplayName("Error Adapter Test")
@ExtendWith(MockitoExtension.class)
class ErrorAdapterTest {

    @InjectMocks
    private ErrorAdapter errorAdapter;
    @Mock
    private ErrorRepository errorRepository;

    @Test
    @DisplayName("should save a given payment error by calling payment repository once.")
    void should_save_given_payment_error() {
        //given
        PaymentId paymentId = new PaymentId(UUID.randomUUID());
        PaymentError paymentError = PaymentError.builder()
                .paymentId(paymentId)
                .error(OTHER)
                .description("Some Error")
                .build();
        //when
        errorAdapter.savePaymentError(paymentError);

        //then
        verify(errorRepository, times(1)).save(any(ErrorEntity.class));
    }
}