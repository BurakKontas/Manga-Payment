package com.aburakkontas.manga_payment.domain.repositories;

import com.aburakkontas.manga_payment.domain.entities.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {


    @Query("SELECT i FROM Item i WHERE i.id IN :ids")
    ArrayList<Item> findByIds(@Param("ids") List<UUID> ids);

}
