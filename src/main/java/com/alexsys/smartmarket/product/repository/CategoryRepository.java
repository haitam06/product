package com.alexsys.smartmarket.product.repository;

import com.alexsys.smartmarket.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
