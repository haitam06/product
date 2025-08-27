package com.alexsys.smartmarket.product.repository;

import com.alexsys.smartmarket.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
