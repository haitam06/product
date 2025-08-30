package com.alexsys.smartmarket.product.controller;

import com.alexsys.smartmarket.product.model.SubCategory;
import com.alexsys.smartmarket.product.service.SubCategoryService;
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
class SubCategoryControllerTest {

    @Mock
    private SubCategoryService subCategoryService;

    @InjectMocks
    private SubCategoryController subCategoryController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SubCategory createTestSubCategory() {
        SubCategory subCategory = new SubCategory();
        subCategory.setId(1);
        subCategory.setName("Smartphones");
        subCategory.setDescription("All kinds of smartphones");
        subCategory.setCategoryId(1);
        return subCategory;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subCategoryController).build();
    }

    @Test
    void getAllSubCategories_shouldReturnListOfSubCategories() {
        SubCategory sub1 = createTestSubCategory();
        SubCategory sub2 = createTestSubCategory();
        sub2.setId(2);
        sub2.setName("Tablets");

        List<SubCategory> subCategories = Arrays.asList(sub1, sub2);
        when(subCategoryService.getAllSubCategories()).thenReturn(subCategories);

        List<SubCategory> result = subCategoryController.getAllSubCategories();

        assertEquals(2, result.size());
        verify(subCategoryService, times(1)).getAllSubCategories();
    }

    @Test
    void getSubCategoryById_shouldReturnSubCategoryWhenExists() {
        SubCategory subCategory = createTestSubCategory();
        when(subCategoryService.getSubCategoryById(1)).thenReturn(Optional.of(subCategory));

        ResponseEntity<SubCategory> response = subCategoryController.getSubCategoryById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(1, response.getBody().getId());
        verify(subCategoryService, times(1)).getSubCategoryById(1);
    }

    @Test
    void getSubCategoryById_shouldReturnNotFoundWhenNotExists() {
        when(subCategoryService.getSubCategoryById(999)).thenReturn(Optional.empty());

        ResponseEntity<SubCategory> response = subCategoryController.getSubCategoryById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(subCategoryService, times(1)).getSubCategoryById(999);
    }

    @Test
    void createSubCategory_shouldReturnCreatedSubCategory() {
        SubCategory subToCreate = createTestSubCategory();
        subToCreate.setId(null);

        SubCategory createdSub = createTestSubCategory();
        when(subCategoryService.saveSubCategory(any(SubCategory.class))).thenReturn(createdSub);

        SubCategory result = subCategoryController.createSubCategory(subToCreate);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(subCategoryService, times(1)).saveSubCategory(subToCreate);
    }

    @Test
    void updateSubCategory_shouldReturnUpdatedSubCategoryWhenExists() {
        SubCategory subDetails = createTestSubCategory();
        subDetails.setName("Wearables");

        SubCategory updatedSub = createTestSubCategory();
        updatedSub.setName("Wearables");

        when(subCategoryService.updateSubCategory(eq(1), any(SubCategory.class))).thenReturn(Optional.of(updatedSub));

        ResponseEntity<SubCategory> response = subCategoryController.updateSubCategory(1, subDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Wearables", response.getBody().getName());
        verify(subCategoryService, times(1)).updateSubCategory(1, subDetails);
    }

    @Test
    void updateSubCategory_shouldReturnNotFoundWhenNotExists() {
        SubCategory subDetails = createTestSubCategory();
        when(subCategoryService.updateSubCategory(eq(999), any(SubCategory.class))).thenReturn(Optional.empty());

        ResponseEntity<SubCategory> response = subCategoryController.updateSubCategory(999, subDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(subCategoryService, times(1)).updateSubCategory(999, subDetails);
    }

    @Test
    void deleteSubCategory_shouldReturnNoContent() {
        ResponseEntity<Void> response = subCategoryController.deleteSubCategory(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(subCategoryService, times(1)).deleteSubCategory(1);
    }

    // MockMvc endpoint tests
    @Test
    void getAllSubCategories_endpoint_shouldReturnOk() throws Exception {
        SubCategory sub = createTestSubCategory();
        when(subCategoryService.getAllSubCategories()).thenReturn(List.of(sub));

        mockMvc.perform(get("/smartmarket/subcategories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Smartphones"));
    }

    @Test
    void getSubCategoryById_endpoint_shouldReturnOk() throws Exception {
        SubCategory sub = createTestSubCategory();
        when(subCategoryService.getSubCategoryById(1)).thenReturn(Optional.of(sub));

        mockMvc.perform(get("/smartmarket/subcategories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Smartphones"));
    }

    @Test
    void getSubCategoryById_endpoint_shouldReturnNotFound() throws Exception {
        when(subCategoryService.getSubCategoryById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/smartmarket/subcategories/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createSubCategory_endpoint_shouldReturnCreated() throws Exception {
        SubCategory subToCreate = createTestSubCategory();
        subToCreate.setId(null);

        SubCategory createdSub = createTestSubCategory();
        when(subCategoryService.saveSubCategory(any(SubCategory.class))).thenReturn(createdSub);

        mockMvc.perform(post("/smartmarket/subcategories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Smartphones"));
    }

    @Test
    void updateSubCategory_endpoint_shouldReturnOk() throws Exception {
        SubCategory subDetails = createTestSubCategory();
        subDetails.setName("Wearables");

        SubCategory updatedSub = createTestSubCategory();
        updatedSub.setName("Wearables");

        when(subCategoryService.updateSubCategory(eq(1), any(SubCategory.class))).thenReturn(Optional.of(updatedSub));

        mockMvc.perform(put("/smartmarket/subcategories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Wearables"));
    }

    @Test
    void deleteSubCategory_endpoint_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/smartmarket/subcategories/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(subCategoryService, times(1)).deleteSubCategory(1);
    }
}
