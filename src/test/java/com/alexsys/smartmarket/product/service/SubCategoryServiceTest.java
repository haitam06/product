package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.SubCategoryMapper;
import com.alexsys.smartmarket.product.model.SubCategory;
import com.alexsys.smartmarket.product.repository.SubCategoryRepository;
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
class SubCategoryServiceTest {

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @Mock
    private SubCategoryMapper subCategoryMapper;

    @InjectMocks
    private SubCategoryService subCategoryService;

    private SubCategory createTestSubCategory() {
        SubCategory subCategory = new SubCategory();
        subCategory.setId(1);
        subCategory.setName("Smartphones");
        subCategory.setDescription("All smartphone devices");
        subCategory.setCategoryId(1);
        return subCategory;
    }

    @BeforeEach
    void setUp() {
        // Optional setup if needed
    }

    @Test
    void getAllSubCategories_shouldReturnAllSubCategories() {
        SubCategory sc1 = createTestSubCategory();
        SubCategory sc2 = createTestSubCategory();
        sc2.setId(2);
        sc2.setName("Laptops");

        when(subCategoryRepository.findAll()).thenReturn(Arrays.asList(sc1, sc2));

        List<SubCategory> subCategories = subCategoryService.getAllSubCategories();

        assertEquals(2, subCategories.size());
        verify(subCategoryRepository, times(1)).findAll();
    }

    @Test
    void getSubCategoryById_shouldReturnSubCategoryWhenExists() {
        SubCategory subCategory = createTestSubCategory();
        when(subCategoryRepository.findById(1)).thenReturn(Optional.of(subCategory));

        Optional<SubCategory> result = subCategoryService.getSubCategoryById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Smartphones", result.get().getName());
        verify(subCategoryRepository, times(1)).findById(1);
    }

    @Test
    void getSubCategoryById_shouldReturnEmptyWhenNotExists() {
        when(subCategoryRepository.findById(999)).thenReturn(Optional.empty());

        Optional<SubCategory> result = subCategoryService.getSubCategoryById(999);

        assertFalse(result.isPresent());
        verify(subCategoryRepository, times(1)).findById(999);
    }

    @Test
    void saveSubCategory_shouldPersistAndReturnSubCategory() {
        SubCategory subCategoryToSave = createTestSubCategory();
        subCategoryToSave.setId(null);

        SubCategory savedSubCategory = createTestSubCategory();
        when(subCategoryRepository.save(subCategoryToSave)).thenReturn(savedSubCategory);

        SubCategory result = subCategoryService.saveSubCategory(subCategoryToSave);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(subCategoryRepository, times(1)).save(subCategoryToSave);
    }

    @Test
    void updateSubCategory_shouldUpdateWhenSubCategoryExists() {
        SubCategory existingSubCategory = createTestSubCategory();
        SubCategory subCategoryDetails = createTestSubCategory();
        subCategoryDetails.setName("Tablets");
        subCategoryDetails.setDescription("All tablet devices");

        when(subCategoryRepository.findById(1)).thenReturn(Optional.of(existingSubCategory));
        when(subCategoryRepository.save(existingSubCategory)).thenReturn(existingSubCategory);

        Optional<SubCategory> result = subCategoryService.updateSubCategory(1, subCategoryDetails);

        assertTrue(result.isPresent());
        verify(subCategoryMapper, times(1)).update(existingSubCategory, subCategoryDetails);
        verify(subCategoryRepository, times(1)).save(existingSubCategory);
        verify(subCategoryRepository, times(1)).findById(1);
    }

    @Test
    void updateSubCategory_shouldReturnEmptyWhenSubCategoryNotExists() {
        SubCategory subCategoryDetails = createTestSubCategory();
        when(subCategoryRepository.findById(999)).thenReturn(Optional.empty());

        Optional<SubCategory> result = subCategoryService.updateSubCategory(999, subCategoryDetails);

        assertFalse(result.isPresent());
        verify(subCategoryRepository, times(1)).findById(999);
        verify(subCategoryMapper, never()).update(any(), any());
        verify(subCategoryRepository, never()).save(any());
    }

    @Test
    void deleteSubCategory_shouldCallRepositoryDelete() {
        subCategoryService.deleteSubCategory(1);
        verify(subCategoryRepository, times(1)).deleteById(1);
    }

    @Test
    void updateSubCategory_shouldOnlyUpdateAllowedFields() {
        SubCategory existingSubCategory = createTestSubCategory();
        SubCategory subCategoryDetails = createTestSubCategory();
        subCategoryDetails.setName("New Name");

        when(subCategoryRepository.findById(1)).thenReturn(Optional.of(existingSubCategory));
        when(subCategoryRepository.save(existingSubCategory)).thenReturn(existingSubCategory);

        Optional<SubCategory> result = subCategoryService.updateSubCategory(1, subCategoryDetails);

        assertTrue(result.isPresent());
        verify(subCategoryMapper, times(1)).update(existingSubCategory, subCategoryDetails);
    }
}
