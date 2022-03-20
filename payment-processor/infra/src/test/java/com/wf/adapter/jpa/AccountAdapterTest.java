package com.wf.adapter.jpa;

import com.wf.adapter.jpa.entity.AccountEntity;
import com.wf.adapter.jpa.repository.AccountRepository;
import com.wf.payments.domain.model.Account;
import com.wf.payments.domain.vo.AccountId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Account Adapter Test")
@ExtendWith(MockitoExtension.class)
class AccountAdapterTest {

    @InjectMocks
    private AccountAdapter accountAdapter;
    @Mock
    private AccountRepository accountRepository;

    @Test
    @DisplayName("should call account repository once to find an account when given an account id")
    void should_find_account_given_accountId() {
        //given
        AccountId accountId = new AccountId(1);

        //when
        accountAdapter.findByAccountId(accountId);

        //then
        verify(accountRepository, times(1)).findByAccountId(anyInt());
    }

    @Test
    @DisplayName("should call account repository to update an account.")
    void should_update_account() {
        //given
        AccountId accountId = new AccountId(1);
        Account account = Account.builder()
                .accountId(accountId)
                .name("wf")
                .email("mail@wf.com")
                .birthdate(LocalDate.of(2000, 1, 1))
                .build();

        //when
        accountAdapter.updateAccount(account);

        //then
        verify(accountRepository, times(1)).save(any(AccountEntity.class));
    }
}