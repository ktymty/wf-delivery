package payments.application.service;

import payments.domain.entity.Payment;

public interface RegisterPayment {
    // register validated payments (offline, crm, shopping)
    void registerOfflinePayment(Payment payment);

    //  validate and register payment (online, visa, paypal.. etc)
    void validateAndRegisterOnlinePayment(Payment payment);
}
