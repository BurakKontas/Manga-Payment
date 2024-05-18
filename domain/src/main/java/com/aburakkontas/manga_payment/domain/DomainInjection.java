package com.aburakkontas.manga_payment.domain;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.aburakkontas.manga_payment.domain.entities")
public class DomainInjection {
}
