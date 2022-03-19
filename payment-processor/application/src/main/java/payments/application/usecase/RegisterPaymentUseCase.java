package payments.application.usecase;

import payments.domain.entity.Payment;

public interface RegisterPaymentUseCase {
    // register validated payments (offline, crm, shopping)
    void registerOfflinePayment(Payment payment);

    //  validate and register payment (online, visa, paypal.. etc)
    void validateAndRegisterOnlinePayment(Payment payment);
}
