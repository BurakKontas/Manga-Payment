package com.aburakkontas.manga_payment.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.aburakkontas.manga_payment.application")
//@EnableJpaRepositories(basePackages = "com.aburakkontas.manga_payment.domain.repositories")
public class ApplicationInjection {
}
