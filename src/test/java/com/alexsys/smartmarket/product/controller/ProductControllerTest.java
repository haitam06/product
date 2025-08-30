package com.alexsys.smartmarket.product.controller;

import com.alexsys.smartmarket.product.model.Product;
import com.alexsys.smartmarket.product.service.ProductService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Product createTestProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("iPhone 15");
        product.setDescription("Latest Apple iPhone");
        product.setSummary("Smartphone");
        product.setCover("iphone15.png");
        product.setCategoryId(1);
        return product;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getAllProducts_shouldReturnListOfProducts() {
        Product product1 = createTestProduct();
        Product product2 = createTestProduct();
        product2.setId(2);
        product2.setName("Samsung Galaxy S24");

        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        List<Product> result = productController.getAllProducts();

        assertEquals(2, result.size());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_shouldReturnProductWhenExists() {
        Product product = createTestProduct();
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        ResponseEntity<Product> response = productController.getProductById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(1, response.getBody().getId());
        verify(productService, times(1)).getProductById(1);
    }

    @Test
    void getProductById_shouldReturnNotFoundWhenNotExists() {
        when(productService.getProductById(999)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.getProductById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(productService, times(1)).getProductById(999);
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() {
        Product productToCreate = createTestProduct();
        productToCreate.setId(null);

        Product createdProduct = createTestProduct();
        when(productService.saveProduct(any(Product.class))).thenReturn(createdProduct);

        Product result = productController.createProduct(productToCreate);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(productService, times(1)).saveProduct(productToCreate);
    }

    @Test
    void updateProduct_shouldReturnUpdatedProductWhenExists() {
        Product productDetails = createTestProduct();
        productDetails.setName("iPhone 15 Pro");

        Product updatedProduct = createTestProduct();
        updatedProduct.setName("iPhone 15 Pro");

        when(productService.updateProduct(eq(1), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        ResponseEntity<Product> response = productController.updateProduct(1, productDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("iPhone 15 Pro", response.getBody().getName());
        verify(productService, times(1)).updateProduct(1, productDetails);
    }

    @Test
    void updateProduct_shouldReturnNotFoundWhenNotExists() {
        Product productDetails = createTestProduct();
        when(productService.updateProduct(eq(999), any(Product.class))).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productController.updateProduct(999, productDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(productService, times(1)).updateProduct(999, productDetails);
    }

    @Test
    void deleteProduct_shouldReturnNoContent() {
        ResponseEntity<Void> response = productController.deleteProduct(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(response.hasBody());
        verify(productService, times(1)).deleteProduct(1);
    }

    // MockMvc endpoint tests
    @Test
    void getAllProducts_endpoint_shouldReturnOk() throws Exception {
        Product product = createTestProduct();
        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/smartmarket/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("iPhone 15"));
    }

    @Test
    void getProductById_endpoint_shouldReturnOk() throws Exception {
        Product product = createTestProduct();
        when(productService.getProductById(1)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/smartmarket/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhone 15"));
    }

    @Test
    void getProductById_endpoint_shouldReturnNotFound() throws Exception {
        when(productService.getProductById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/smartmarket/products/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_endpoint_shouldReturnCreated() throws Exception {
        Product productToCreate = createTestProduct();
        productToCreate.setId(null);

        Product createdProduct = createTestProduct();
        when(productService.saveProduct(any(Product.class))).thenReturn(createdProduct);

        mockMvc.perform(post("/smartmarket/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhone 15"));
    }

    @Test
    void updateProduct_endpoint_shouldReturnOk() throws Exception {
        Product productDetails = createTestProduct();
        productDetails.setName("iPhone 15 Pro");

        Product updatedProduct = createTestProduct();
        updatedProduct.setName("iPhone 15 Pro");

        when(productService.updateProduct(eq(1), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        mockMvc.perform(put("/smartmarket/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15 Pro"));
    }

    @Test
    void deleteProduct_endpoint_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/smartmarket/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1);
    }
}
