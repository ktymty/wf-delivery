package adapter.jpa;

import adapter.jpa.entity.PaymentEntity;
import adapter.jpa.repository.PaymentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import payments.application.port.PaymentPort;
import payments.domain.model.Payment;
import payments.domain.vo.PaymentId;

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

    private PaymentEntity toPaymentEntity(Payment payment){
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
