package com.wf.payments.application.usecase;

import com.wf.payments.domain.model.Payment;

public interface RegisterPaymentUseCase {
    // register validated payments (offline, crm, shopping)
    void registerOfflinePayment(Payment payment);

    //  validate and register payment (online, visa, paypal.. etc)
    void validateAndRegisterOnlinePayment(Payment payment);
}
