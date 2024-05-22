package com.aburakkontas.manga_payment.domain.entities.item;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "items")
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private Double price;
    private String category = "Credit";
    private String description;
    private UUID imageId;
    private String itemType = "VIRTUAL";
}
