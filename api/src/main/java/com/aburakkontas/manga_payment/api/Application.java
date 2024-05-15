package com.aburakkontas.manga_payment.api;

import com.aburakkontas.manga_payment.application.ApplicationInjection;
import com.aburakkontas.manga_payment.infrastructure.InfrastructureInjection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ApplicationInjection.class, InfrastructureInjection.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
