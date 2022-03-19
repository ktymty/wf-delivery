package com.wf.adapter.jpa.repository;

import com.wf.adapter.jpa.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity findByAccountId(Integer accountId);
}
