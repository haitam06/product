package com.alexsys.smartmarket.product.controller;

import com.alexsys.smartmarket.enums.ProductAttributeType;
import com.alexsys.smartmarket.product.model.ProductAttribute;
import com.alexsys.smartmarket.product.service.ProductAttributeService;
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
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductAttributeControllerTest {

    @Mock
    private ProductAttributeService productAttributeService;

    @InjectMocks
    private ProductAttributeController productAttributeController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductAttribute createTestAttribute() {
        ProductAttribute attribute = new ProductAttribute();
        attribute.setId(1);
        attribute.setValue("Red");
        attribute.setType(ProductAttributeType.COLOR);
        attribute.setProductId(1);
        return attribute;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productAttributeController).build();
    }

    @Test
    void getAllProductAttributes_shouldReturnListOfAttributes() {
        ProductAttribute attr1 = createTestAttribute();
        ProductAttribute attr2 = createTestAttribute();
        attr2.setId(2);
        attr2.setValue("Blue");

        List<ProductAttribute> attributes = Arrays.asList(attr1, attr2);
        when(productAttributeService.getAllProductAttributes()).thenReturn(attributes);

        List<ProductAttribute> result = productAttributeController.getAllProductAttributes();

        assertEquals(2, result.size());
        verify(productAttributeService, times(1)).getAllProductAttributes();
    }

    @Test
    void getProductAttributeById_shouldReturnAttributeWhenExists() {
        ProductAttribute attribute = createTestAttribute();
        when(productAttributeService.getProductAttributeById(1)).thenReturn(Optional.of(attribute));

        ResponseEntity<ProductAttribute> response = productAttributeController.getProductAttributeById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(1, response.getBody().getId());
        verify(productAttributeService, times(1)).getProductAttributeById(1);
    }

    @Test
    void getProductAttributeById_shouldReturnNotFoundWhenNotExists() {
        when(productAttributeService.getProductAttributeById(999)).thenReturn(Optional.empty());

        ResponseEntity<ProductAttribute> response = productAttributeController.getProductAttributeById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(productAttributeService, times(1)).getProductAttributeById(999);
    }

    @Test
    void createProductAttribute_shouldReturnCreatedAttribute() {
        ProductAttribute attributeToCreate = createTestAttribute();
        attributeToCreate.setId(null);

        ProductAttribute createdAttribute = createTestAttribute();
        when(productAttributeService.saveProductAttribute(any(ProductAttribute.class))).thenReturn(createdAttribute);

        ProductAttribute result = productAttributeController.createProductAttribute(attributeToCreate);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(productAttributeService, times(1)).saveProductAttribute(attributeToCreate);
    }

    @Test
    void updateProductAttribute_shouldReturnUpdatedAttributeWhenExists() {
        ProductAttribute attributeDetails = createTestAttribute();
        attributeDetails.setValue("Green");

        ProductAttribute updatedAttribute = createTestAttribute();
        updatedAttribute.setValue("Green");

        when(productAttributeService.updateProductAttribute(eq(1), any(ProductAttribute.class))).thenReturn(Optional.of(updatedAttribute));

        ResponseEntity<ProductAttribute> response = productAttributeController.updateProductAttribute(1, attributeDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Green", response.getBody().getValue());
        verify(productAttributeService, times(1)).updateProductAttribute(1, attributeDetails);
    }

    @Test
    void updateProductAttribute_shouldReturnNotFoundWhenNotExists() {
        ProductAttribute attributeDetails = createTestAttribute();
        when(productAttributeService.updateProductAttribute(eq(999), any(ProductAttribute.class))).thenReturn(Optional.empty());

        ResponseEntity<ProductAttribute> response = productAttributeController.updateProductAttribute(999, attributeDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(productAttributeService, times(1)).updateProductAttribute(999, attributeDetails);
    }

    @Test
    void deleteProductAttribute_shouldReturnNoContent() {
        ResponseEntity<Void> response = productAttributeController.deleteProductAttribute(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(productAttributeService, times(1)).deleteProductAttribute(1);
    }

    // MockMvc endpoint tests
    @Test
    void getAllProductAttributes_endpoint_shouldReturnOk() throws Exception {
        ProductAttribute attribute = createTestAttribute();
        when(productAttributeService.getAllProductAttributes()).thenReturn(List.of(attribute));

        mockMvc.perform(get("/smartmarket/product-attributes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].value").value("Red"));
    }

    @Test
    void getProductAttributeById_endpoint_shouldReturnOk() throws Exception {
        ProductAttribute attribute = createTestAttribute();
        when(productAttributeService.getProductAttributeById(1)).thenReturn(Optional.of(attribute));

        mockMvc.perform(get("/smartmarket/product-attributes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.value").value("Red"));
    }

    @Test
    void getProductAttributeById_endpoint_shouldReturnNotFound() throws Exception {
        when(productAttributeService.getProductAttributeById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/smartmarket/product-attributes/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProductAttribute_endpoint_shouldReturnCreated() throws Exception {
        ProductAttribute attributeToCreate = createTestAttribute();
        attributeToCreate.setId(null);

        ProductAttribute createdAttribute = createTestAttribute();
        when(productAttributeService.saveProductAttribute(any(ProductAttribute.class))).thenReturn(createdAttribute);

        mockMvc.perform(post("/smartmarket/product-attributes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attributeToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.value").value("Red"));
    }

    @Test
    void updateProductAttribute_endpoint_shouldReturnOk() throws Exception {
        ProductAttribute attributeDetails = createTestAttribute();
        attributeDetails.setValue("Green");

        ProductAttribute updatedAttribute = createTestAttribute();
        updatedAttribute.setValue("Green");

        when(productAttributeService.updateProductAttribute(eq(1), any(ProductAttribute.class))).thenReturn(Optional.of(updatedAttribute));

        mockMvc.perform(put("/smartmarket/product-attributes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attributeDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value("Green"));
    }

    @Test
    void deleteProductAttribute_endpoint_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/smartmarket/product-attributes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productAttributeService, times(1)).deleteProductAttribute(1);
    }
}
