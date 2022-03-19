package payments.application.repository;

import payments.domain.entity.Account;
import payments.domain.vo.AccountId;

public interface AccountRepository {
    Account fetch(AccountId accountId);

    void update(Account account);
}
