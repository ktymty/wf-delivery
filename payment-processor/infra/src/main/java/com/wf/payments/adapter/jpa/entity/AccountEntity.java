package com.wf.payments.adapter.jpa.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "accounts")
public class AccountEntity {
    @Id
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "last_payment_date")
    private LocalDateTime lastPaymentDate;
}
