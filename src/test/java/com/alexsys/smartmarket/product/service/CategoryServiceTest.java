package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.CategoryMapper;
import com.alexsys.smartmarket.product.model.Category;
import com.alexsys.smartmarket.product.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category createTestCategory() {
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");
        category.setDescription("Electronic gadgets and devices");
        return category;
    }

    @BeforeEach
    void setUp() {
        // Optional setup if needed
    }

    @Test
    void getAllCategories_shouldReturnAllCategories() {
        Category cat1 = createTestCategory();
        Category cat2 = createTestCategory();
        cat2.setId(2);
        cat2.setName("Clothing");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategoryById_shouldReturnCategoryWhenExists() {
        Category category = createTestCategory();
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Electronics", result.get().getName());
        verify(categoryRepository, times(1)).findById(1);
    }

    @Test
    void getCategoryById_shouldReturnEmptyWhenNotExists() {
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoryById(999);

        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).findById(999);
    }

    @Test
    void saveCategory_shouldPersistAndReturnCategory() {
        Category categoryToSave = createTestCategory();
        categoryToSave.setId(null);

        Category savedCategory = createTestCategory();
        when(categoryRepository.save(categoryToSave)).thenReturn(savedCategory);

        Category result = categoryService.saveCategory(categoryToSave);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(categoryRepository, times(1)).save(categoryToSave);
    }

    @Test
    void updateCategory_shouldUpdateWhenCategoryExists() {
        Category existingCategory = createTestCategory();
        Category categoryDetails = createTestCategory();
        categoryDetails.setName("Home Appliances");
        categoryDetails.setDescription("Appliances for home use");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        Optional<Category> result = categoryService.updateCategory(1, categoryDetails);

        assertTrue(result.isPresent());
        verify(categoryMapper, times(1)).update(existingCategory, categoryDetails);
        verify(categoryRepository, times(1)).save(existingCategory);
        verify(categoryRepository, times(1)).findById(1);
    }

    @Test
    void updateCategory_shouldReturnEmptyWhenCategoryNotExists() {
        Category categoryDetails = createTestCategory();
        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.updateCategory(999, categoryDetails);

        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).findById(999);
        verify(categoryMapper, never()).update(any(), any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void deleteCategory_shouldCallRepositoryDelete() {
        categoryService.deleteCategory(1);
        verify(categoryRepository, times(1)).deleteById(1);
    }

    @Test
    void updateCategory_shouldOnlyUpdateAllowedFields() {
        Category existingCategory = createTestCategory();
        Category categoryDetails = createTestCategory();
        categoryDetails.setName("New Name");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);

        Optional<Category> result = categoryService.updateCategory(1, categoryDetails);

        assertTrue(result.isPresent());
        verify(categoryMapper, times(1)).update(existingCategory, categoryDetails);
    }
}
