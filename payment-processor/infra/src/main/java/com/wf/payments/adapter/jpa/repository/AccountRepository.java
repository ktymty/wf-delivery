package com.wf.payments.adapter.jpa.repository;

import com.wf.payments.adapter.jpa.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity findByAccountId(Integer accountId);
}
