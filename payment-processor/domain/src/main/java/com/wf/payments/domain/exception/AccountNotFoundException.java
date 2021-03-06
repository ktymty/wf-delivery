package com.wf.payments.domain.exception;

import lombok.NonNull;
import com.wf.payments.domain.model.PaymentError;
import com.wf.payments.domain.vo.AccountId;
import com.wf.payments.domain.vo.ErrorType;
import com.wf.payments.domain.vo.PaymentId;

public class AccountNotFoundException extends PaymentException {

    private final PaymentId paymentId;

    private final AccountId accountId;

    public AccountNotFoundException(@NonNull PaymentId paymentId, @NonNull AccountId accountId) {
        super(String.format("Payment %s - Account %d not found.", paymentId.getId(), accountId.getId()));
        this.paymentId = paymentId;
        this.accountId = accountId;
    }

    @Override
    public PaymentError toError() {
        return PaymentError.builder()
                .paymentId(paymentId)
                .error(ErrorType.DATABASE)
                .description(String.format("Account %d does not exists.", accountId.getId()))
                .build();
    }
}
