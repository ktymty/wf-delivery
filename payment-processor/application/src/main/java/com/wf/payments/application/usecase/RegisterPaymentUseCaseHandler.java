package com.wf.payments.application.usecase;

import com.wf.payments.application.port.AccountPort;
import com.wf.payments.application.port.PaymentErrorLogPort;
import com.wf.payments.application.port.PaymentGatewayPort;
import com.wf.payments.application.port.PaymentPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wf.payments.domain.model.Account;
import com.wf.payments.domain.model.Payment;
import com.wf.payments.domain.model.PaymentError;
import com.wf.payments.domain.exception.AccountNotFoundException;
import com.wf.payments.domain.exception.InvalidPaymentException;
import com.wf.payments.domain.exception.PaymentException;
import com.wf.payments.domain.vo.ErrorType;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterPaymentUseCaseHandler implements RegisterPaymentUseCase {
    @NonNull
    private final PaymentPort paymentPort;
    @NonNull
    private final AccountPort accountPort;
    @NonNull
    private final PaymentGatewayPort paymentGatewayPort;
    @NonNull
    private final PaymentErrorLogPort paymentErrorLogPort;

    @Transactional
    @Override
    public void registerOfflinePayment(Payment payment) {
        try {
            if (!paymentPort.exists(payment.getPaymentId())) {
                savePayment(payment);
            } else {
                log.info("Payment {} already exists", payment.getPaymentId());
            }
        } catch (PaymentException pe) {
            paymentErrorLogPort.savePaymentError(pe.toError());
        } catch (Exception ex) {
            paymentErrorLogPort.savePaymentError(PaymentError.builder()
                    .paymentId(payment.getPaymentId())
                    .error(ErrorType.OTHER)
                    .description(ex.getMessage())
                    .build());
        }
    }

    @Transactional
    @Override
    public void validateAndRegisterOnlinePayment(Payment payment) {
        try {
            if (!paymentPort.exists(payment.getPaymentId())) {
                saveIfValidPayment(payment);
            } else {
                log.info("Payment {} already exists", payment.getPaymentId());
            }
        } catch (PaymentException pe) {
            paymentErrorLogPort.savePaymentError(pe.toError());
        } catch (Exception ex) {
            paymentErrorLogPort.savePaymentError(PaymentError.builder()
                    .paymentId(payment.getPaymentId())
                    .error(ErrorType.OTHER)
                    .description(ex.getMessage())
                    .build());
        }
    }

    private void savePayment(Payment payment) {
        Account account = accountPort.findByAccountId(payment.getAccountId());
        if (Objects.isNull(account)) {
            throw new AccountNotFoundException(payment.getPaymentId(), payment.getAccountId());
        }
        paymentPort.savePayment(payment);

        account.addPayment(payment);
        accountPort.updateAccount(account);
    }

    private void saveIfValidPayment(Payment payment) {
        if (paymentGatewayPort.isPaymentValid(payment)) {
            savePayment(payment);
        } else {
            throw new InvalidPaymentException(payment.getPaymentId());
        }
    }
}
