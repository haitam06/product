package com.alexsys.smartmarket.product.repository;

import com.alexsys.smartmarket.product.model.ProductsSku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsSkuRepository extends JpaRepository<ProductsSku, Integer> {
}
