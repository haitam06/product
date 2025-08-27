package com.alexsys.smartmarket.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products_skus")
@Setter
@Getter
public class ProductsSku {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "size_attribute_id")
    private Integer sizeAttributeId;

    @Column(name = "color_attribute_id")
    private Integer colorAttributeId;

    private String sku;

    private Double price;

    private Integer quantity;
}
