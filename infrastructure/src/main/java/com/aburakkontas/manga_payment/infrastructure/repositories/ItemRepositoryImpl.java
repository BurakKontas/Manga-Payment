package com.aburakkontas.manga_payment.infrastructure.repositories;

import com.aburakkontas.manga_payment.domain.repositories.ItemRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface ItemRepositoryImpl extends ItemRepository {
}
