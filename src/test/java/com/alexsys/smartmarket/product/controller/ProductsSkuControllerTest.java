package com.alexsys.smartmarket.product.controller;

import com.alexsys.smartmarket.product.model.ProductsSku;
import com.alexsys.smartmarket.product.service.ProductsSkuService;
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
class ProductsSkuControllerTest {

    @Mock
    private ProductsSkuService productsSkuService;

    @InjectMocks
    private ProductsSkuController productsSkuController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductsSku createTestSku() {
        ProductsSku sku = new ProductsSku();
        sku.setId(1);
        sku.setProductId(1);
        sku.setSizeAttributeId(1);
        sku.setColorAttributeId(2);
        sku.setSku("SKU-001");
        sku.setPrice(99.99);
        sku.setQuantity(10);
        return sku;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productsSkuController).build();
    }

    @Test
    void getAllProductSkus_shouldReturnListOfSkus() {
        ProductsSku sku1 = createTestSku();
        ProductsSku sku2 = createTestSku();
        sku2.setId(2);
        sku2.setSku("SKU-002");

        List<ProductsSku> skus = Arrays.asList(sku1, sku2);
        when(productsSkuService.getAllProductsSkus()).thenReturn(skus);

        List<ProductsSku> result = productsSkuController.getAllProductSkus();

        assertEquals(2, result.size());
        verify(productsSkuService, times(1)).getAllProductsSkus();
    }

    @Test
    void getProductSkuById_shouldReturnSkuWhenExists() {
        ProductsSku sku = createTestSku();
        when(productsSkuService.getProductsSkuById(1)).thenReturn(Optional.of(sku));

        ResponseEntity<ProductsSku> response = productsSkuController.getProductSkuById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(1, response.getBody().getId());
        verify(productsSkuService, times(1)).getProductsSkuById(1);
    }

    @Test
    void getProductSkuById_shouldReturnNotFoundWhenNotExists() {
        when(productsSkuService.getProductsSkuById(999)).thenReturn(Optional.empty());

        ResponseEntity<ProductsSku> response = productsSkuController.getProductSkuById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(productsSkuService, times(1)).getProductsSkuById(999);
    }

    @Test
    void createProductSku_shouldReturnCreatedSku() {
        ProductsSku skuToCreate = createTestSku();
        skuToCreate.setId(null);

        ProductsSku createdSku = createTestSku();
        when(productsSkuService.saveProductsSku(any(ProductsSku.class))).thenReturn(createdSku);

        ProductsSku result = productsSkuController.createProductSku(skuToCreate);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(productsSkuService, times(1)).saveProductsSku(skuToCreate);
    }

    @Test
    void updateProductsSku_shouldReturnUpdatedSkuWhenExists() {
        ProductsSku skuDetails = createTestSku();
        skuDetails.setSku("SKU-Updated");

        ProductsSku updatedSku = createTestSku();
        updatedSku.setSku("SKU-Updated");

        when(productsSkuService.updateProductsSku(eq(1), any(ProductsSku.class))).thenReturn(Optional.of(updatedSku));

        ResponseEntity<ProductsSku> response = productsSkuController.updateProductsSku(1, skuDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("SKU-Updated", response.getBody().getSku());
        verify(productsSkuService, times(1)).updateProductsSku(1, skuDetails);
    }

    @Test
    void updateProductsSku_shouldReturnNotFoundWhenNotExists() {
        ProductsSku skuDetails = createTestSku();
        when(productsSkuService.updateProductsSku(eq(999), any(ProductsSku.class))).thenReturn(Optional.empty());

        ResponseEntity<ProductsSku> response = productsSkuController.updateProductsSku(999, skuDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(productsSkuService, times(1)).updateProductsSku(999, skuDetails);
    }

    @Test
    void deleteProductsSku_shouldReturnNoContent() {
        ResponseEntity<Void> response = productsSkuController.deleteProductSku(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(productsSkuService, times(1)).deleteProductsSku(1);
    }

    // MockMvc endpoint tests
    @Test
    void getAllProductSkus_endpoint_shouldReturnOk() throws Exception {
        ProductsSku sku = createTestSku();
        when(productsSkuService.getAllProductsSkus()).thenReturn(List.of(sku));

        mockMvc.perform(get("/smartmarket/product-skus")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sku").value("SKU-001"));
    }

    @Test
    void getProductSkuById_endpoint_shouldReturnOk() throws Exception {
        ProductsSku sku = createTestSku();
        when(productsSkuService.getProductsSkuById(1)).thenReturn(Optional.of(sku));

        mockMvc.perform(get("/smartmarket/product-skus/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sku").value("SKU-001"));
    }

    @Test
    void getProductSkuById_endpoint_shouldReturnNotFound() throws Exception {
        when(productsSkuService.getProductsSkuById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/smartmarket/product-skus/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProductSku_endpoint_shouldReturnCreated() throws Exception {
        ProductsSku skuToCreate = createTestSku();
        skuToCreate.setId(null);

        ProductsSku createdSku = createTestSku();
        when(productsSkuService.saveProductsSku(any(ProductsSku.class))).thenReturn(createdSku);

        mockMvc.perform(post("/smartmarket/product-skus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skuToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sku").value("SKU-001"));
    }

    @Test
    void updateProductsSku_endpoint_shouldReturnOk() throws Exception {
        ProductsSku skuDetails = createTestSku();
        skuDetails.setSku("SKU-Updated");

        ProductsSku updatedSku = createTestSku();
        updatedSku.setSku("SKU-Updated");

        when(productsSkuService.updateProductsSku(eq(1), any(ProductsSku.class))).thenReturn(Optional.of(updatedSku));

        mockMvc.perform(put("/smartmarket/product-skus/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(skuDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-Updated"));
    }

    @Test
    void deleteProductsSku_endpoint_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/smartmarket/product-skus/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productsSkuService, times(1)).deleteProductsSku(1);
    }
}
