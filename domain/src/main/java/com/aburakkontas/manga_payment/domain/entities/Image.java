package com.aburakkontas.manga_payment.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String type;

    @Lob
    @Column(name = "data" , length = 2000000000)
    public byte[] data;
}
