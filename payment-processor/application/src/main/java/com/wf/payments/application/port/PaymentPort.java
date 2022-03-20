package com.wf.payments.application.port;

import com.wf.payments.domain.model.Payment;
import com.wf.payments.domain.vo.PaymentId;

public interface PaymentPort {
    boolean exists(PaymentId paymentId);

    void savePayment(Payment payment);
}
