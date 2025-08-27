package com.alexsys.smartmarket.product.repository;

import com.alexsys.smartmarket.product.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {
}
