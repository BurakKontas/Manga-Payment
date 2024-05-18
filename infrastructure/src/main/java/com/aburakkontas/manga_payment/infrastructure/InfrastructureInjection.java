package com.aburakkontas.manga_payment.infrastructure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.aburakkontas.manga_payment.infrastructure")
@EnableJpaRepositories("com.aburakkontas.manga_payment.infrastructure.repositories")
public class InfrastructureInjection {

}
