package payments.application.services;

import payments.domain.entities.Payment;

public interface RegisterPayment {
    // register validated payments (offline, crm, shopping)
    void registerPayment(Payment payment);

    //  validate and register payment (online, visa, paypal.. etc)
    void validateAndRegisterPayment(Payment payment);
}
