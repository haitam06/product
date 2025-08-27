package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.CategoryMapper;
import com.alexsys.smartmarket.product.model.Category;
import com.alexsys.smartmarket.product.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<Category> getAllCategories() { return categoryRepository.findAll(); }
    public Optional<Category> getCategoryById(Integer id) { return categoryRepository.findById(id); }
    public Category saveCategory(Category category) { return categoryRepository.save(category); }
    public Optional<Category> updateCategory(Integer id, Category categoryDetails) {
        var existingCategoryOptional = getCategoryById(id);
        if (existingCategoryOptional.isEmpty()) {
            return Optional.empty();
        }
        var existingCategory = existingCategoryOptional.get();
        categoryMapper.update(existingCategory, categoryDetails);
        return Optional.ofNullable(categoryRepository.save(existingCategory));
    }
    public void deleteCategory(Integer id) { categoryRepository.deleteById(id); }
}
