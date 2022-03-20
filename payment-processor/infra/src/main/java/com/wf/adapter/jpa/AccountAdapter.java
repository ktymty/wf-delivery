package com.wf.adapter.jpa;

import com.wf.adapter.jpa.entity.AccountEntity;
import com.wf.adapter.jpa.repository.AccountRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import com.wf.payments.application.port.AccountPort;
import com.wf.payments.domain.model.Account;
import com.wf.payments.domain.vo.AccountId;

import javax.persistence.LockModeType;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountAdapter implements AccountPort {
    @NonNull
    private final AccountRepository accountRepository;

    // locking the record as it will be updated
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public Account findByAccountId(AccountId accountId) {
        return toAccount(accountRepository.findByAccountId(accountId.getId()));
    }

    @Override
    public void updateAccount(Account account) {
        accountRepository.save(toAccountEntity(account));
    }

    private Account toAccount(AccountEntity accountEntity) {
        return Optional.ofNullable(accountEntity).map(ae -> Account.builder()
                        .accountId(new AccountId(ae.getAccountId()))
                        .name(ae.getName())
                        .email(ae.getEmail())
                        .birthdate(ae.getBirthdate())
                        .lastPaymentDate(ae.getLastPaymentDate())
                        .build())
                .orElse(null);
    }

    private AccountEntity toAccountEntity(Account account) {
        return AccountEntity.builder()
                .accountId(account.getAccountId().getId())
                .name(account.getName())
                .email(account.getEmail())
                .birthdate(account.getBirthdate())
                .lastPaymentDate(account.getLastPaymentDate())
                .build();
    }
}
