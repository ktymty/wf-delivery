package com.wf.adapter.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "errors")
public class ErrorEntity {
    @Id
    @Column(name = "payment_id", length = 100)
    private String paymentId;

    @Column(name = "error", nullable = false, length = 10)
    private String error;

    @Column(name = "error_description", nullable = false, length = 200)
    private String errorDescription;
}
