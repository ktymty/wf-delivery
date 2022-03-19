package payments.application.port;

import payments.domain.model.Account;
import payments.domain.vo.AccountId;

public interface AccountPort {
    Account findByAccountId(AccountId accountId);

    void updateAccount(Account account);
}
