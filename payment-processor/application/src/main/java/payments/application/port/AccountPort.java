package payments.application.port;

import payments.domain.entity.Account;
import payments.domain.model.AccountId;

public interface AccountPort {
    Account fetch(AccountId accountId);

    void update(Account account);
}
