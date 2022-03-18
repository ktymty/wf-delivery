package payments.application.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payments.application.repositories.AccountRepository;
import payments.application.repositories.PaymentErrorLogRepository;
import payments.application.client.PaymentGatewayClient;
import payments.application.repositories.PaymentRepository;
import payments.domain.entities.Account;
import payments.domain.entities.Payment;
import payments.domain.entities.PaymentError;
import payments.domain.exceptions.AccountNotFoundException;
import payments.domain.exceptions.InvalidPaymentException;
import payments.domain.exceptions.PaymentException;
import payments.domain.vo.ErrorType;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterPaymentService implements RegisterPayment{
    @NonNull
    private final PaymentRepository paymentRepository;
    @NonNull
    private final AccountRepository accountRepository;
    @NonNull
    private final PaymentGatewayClient paymentGatewayClient;
    @NonNull
    private final PaymentErrorLogRepository paymentErrorLogRepository;

    @Transactional
    @Override
    public void registerPayment(Payment payment) {
        try {
            if (!paymentRepository.exists(payment.getPaymentId())) {
                savePayment(payment);
            } else {
                log.info("Payment {} already exists", payment.getPaymentId());
            }
        } catch (PaymentException pe) {
            paymentErrorLogRepository.save(pe.toError());
        } catch (Exception ex) {
            paymentErrorLogRepository.save(PaymentError.builder()
                    .paymentId(payment.getPaymentId())
                    .error(ErrorType.OTHER)
                    .description(ex.getMessage())
                    .build());
        }
    }

    @Transactional
    @Override
    public void validateAndRegisterPayment(Payment payment) {
        try {
            if(!paymentRepository.exists(payment.getPaymentId())) {
                saveIfValidPayment(payment);
            } else {
                log.info("Payment {} already exists", payment.getPaymentId());
            }
        } catch (PaymentException pe) {
            paymentErrorLogRepository.save(pe.toError());
        } catch (Exception ex) {
            paymentErrorLogRepository.save(PaymentError.builder()
                    .paymentId(payment.getPaymentId())
                    .error(ErrorType.OTHER)
                    .description(ex.getMessage())
                    .build());
        }
    }

    private void savePayment(Payment payment) {
        Account account = accountRepository.fetch(payment.getAccountId());
        if (Objects.isNull(account)) {
            throw new AccountNotFoundException(payment.getPaymentId(), payment.getAccountId());
        }
        paymentRepository.save(payment);

        account.addPayment(payment);
        accountRepository.update(account);
    }

    private void saveIfValidPayment(Payment payment) {
        if(paymentGatewayClient.isPaymentValid(payment)) {
            savePayment(payment);
        } else {
            throw new InvalidPaymentException(payment.getPaymentId());
        }
    }
}
