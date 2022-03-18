package payments.application.repositories;

import payments.domain.entities.Account;
import payments.domain.vo.AccountId;

public interface AccountRepository {
    Account fetch(AccountId accountId);
    void update(Account account);
}
