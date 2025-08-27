package com.alexsys.smartmarket.product.model;

import com.alexsys.smartmarket.enums.ProductAttributeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_attributes")
@Setter
@Getter
public class ProductAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String value;

    @Enumerated(EnumType.STRING)
    private ProductAttributeType type;

    @Column(name = "product_id", nullable = false)
    private Integer productId;
}
