package com.wf.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {
        "com.wf.payments.launcher",
        "com.wf.payments.application.usecase",
        "com.wf.adapter.kafka.consumer",
        "com.wf.adapter.jpa",
        "com.wf.adapter.rest"
})
@EntityScan( basePackages = { "com.wf.payments.jpa.entity" })
public class PaymentsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentsApplication.class, args);
    }
}
