package com.wf.payments.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan(basePackages = {
        "com.wf.payments.launcher",
        "com.wf.payments.application.usecase",
        "com.wf.payments.adapter.kafka.consumer",
        "com.wf.payments.adapter.jpa.service",
        "com.wf.payments.adapter.rest.client"})
@EntityScan(basePackages = "com.wf.payments.adapter.jpa.entity")
@EnableJpaRepositories(basePackages = "com.wf.payments.adapter.jpa.repository")
public class PaymentsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentsApplication.class, args);
    }
}
