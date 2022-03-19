package com.wf.payments.application.port;

import com.wf.payments.domain.model.Account;
import com.wf.payments.domain.vo.AccountId;

public interface AccountPort {
    Account findByAccountId(AccountId accountId);

    void updateAccount(Account account);
}
