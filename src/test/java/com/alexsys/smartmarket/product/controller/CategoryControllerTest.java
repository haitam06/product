package com.alexsys.smartmarket.product.controller;

import com.alexsys.smartmarket.product.model.Category;
import com.alexsys.smartmarket.product.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Category createTestCategory() {
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");
        category.setDescription("Electronic devices and gadgets");
        return category;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void getAllCategories_shouldReturnListOfCategories() {
        Category category1 = createTestCategory();
        Category category2 = createTestCategory();
        category2.setId(2);
        category2.setName("Books");

        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryService.getAllCategories()).thenReturn(categories);

        List<Category> result = categoryController.getAllCategories();

        assertEquals(2, result.size());
        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_shouldReturnCategoryWhenExists() {
        Category category = createTestCategory();
        when(categoryService.getCategoryById(1)).thenReturn(Optional.of(category));

        ResponseEntity<Category> response = categoryController.getCategoryById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(1, response.getBody().getId());
        verify(categoryService, times(1)).getCategoryById(1);
    }

    @Test
    void getCategoryById_shouldReturnNotFoundWhenNotExists() {
        when(categoryService.getCategoryById(999)).thenReturn(Optional.empty());

        ResponseEntity<Category> response = categoryController.getCategoryById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(categoryService, times(1)).getCategoryById(999);
    }

    @Test
    void createCategory_shouldReturnCreatedCategory() {
        Category categoryToCreate = createTestCategory();
        categoryToCreate.setId(null);

        Category createdCategory = createTestCategory();
        when(categoryService.saveCategory(any(Category.class))).thenReturn(createdCategory);

        Category result = categoryController.createCategory(categoryToCreate);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(categoryService, times(1)).saveCategory(categoryToCreate);
    }

    @Test
    void updateCategory_shouldReturnUpdatedCategoryWhenExists() {
        Category categoryDetails = createTestCategory();
        categoryDetails.setName("Home Appliances");

        Category updatedCategory = createTestCategory();
        updatedCategory.setName("Home Appliances");

        when(categoryService.updateCategory(eq(1), any(Category.class))).thenReturn(Optional.of(updatedCategory));

        ResponseEntity<Category> response = categoryController.updateCategory(1, categoryDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Home Appliances", response.getBody().getName());
        verify(categoryService, times(1)).updateCategory(1, categoryDetails);
    }

    @Test
    void updateCategory_shouldReturnNotFoundWhenNotExists() {
        Category categoryDetails = createTestCategory();
        when(categoryService.updateCategory(eq(999), any(Category.class))).thenReturn(Optional.empty());

        ResponseEntity<Category> response = categoryController.updateCategory(999, categoryDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(categoryService, times(1)).updateCategory(999, categoryDetails);
    }

    @Test
    void deleteCategory_shouldReturnNoContent() {
        ResponseEntity<Void> response = categoryController.deleteCategory(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(categoryService, times(1)).deleteCategory(1);
    }

    // MockMvc endpoint tests
    @Test
    void getAllCategories_endpoint_shouldReturnOk() throws Exception {
        Category category = createTestCategory();
        when(categoryService.getAllCategories()).thenReturn(List.of(category));

        mockMvc.perform(get("/smartmarket/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    @Test
    void getCategoryById_endpoint_shouldReturnOk() throws Exception {
        Category category = createTestCategory();
        when(categoryService.getCategoryById(1)).thenReturn(Optional.of(category));

        mockMvc.perform(get("/smartmarket/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void getCategoryById_endpoint_shouldReturnNotFound() throws Exception {
        when(categoryService.getCategoryById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/smartmarket/categories/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCategory_endpoint_shouldReturnCreated() throws Exception {
        Category categoryToCreate = createTestCategory();
        categoryToCreate.setId(null);

        Category createdCategory = createTestCategory();
        when(categoryService.saveCategory(any(Category.class))).thenReturn(createdCategory);

        mockMvc.perform(post("/smartmarket/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void updateCategory_endpoint_shouldReturnOk() throws Exception {
        Category categoryDetails = createTestCategory();
        categoryDetails.setName("Home Appliances");

        Category updatedCategory = createTestCategory();
        updatedCategory.setName("Home Appliances");

        when(categoryService.updateCategory(eq(1), any(Category.class))).thenReturn(Optional.of(updatedCategory));

        mockMvc.perform(put("/smartmarket/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Home Appliances"));
    }

    @Test
    void deleteCategory_endpoint_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/smartmarket/categories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1);
    }
}
