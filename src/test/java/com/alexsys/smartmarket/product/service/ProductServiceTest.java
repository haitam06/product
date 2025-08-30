package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.ProductMapper;
import com.alexsys.smartmarket.product.model.Product;
import com.alexsys.smartmarket.product.repository.ProductRepository;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product createTestProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Product One");
        product.setDescription("Description One");
        product.setSummary("Summary One");
        product.setCover("cover1.jpg");
        product.setCategoryId(1);
        return product;
    }

    @BeforeEach
    void setUp() {
        // optional: reset mocks if needed
    }

    @Test
    void getAllProducts_shouldReturnAllProducts() {
        Product p1 = createTestProduct();
        Product p2 = createTestProduct();
        p2.setId(2);
        p2.setName("Product Two");

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_shouldReturnProductWhenExists() {
        Product product = createTestProduct();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Product One", result.get().getName());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void getProductById_shouldReturnEmptyWhenNotExists() {
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById(999);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(999);
    }

    @Test
    void saveProduct_shouldPersistAndReturnProduct() {
        Product productToSave = createTestProduct();
        productToSave.setId(null);

        Product savedProduct = createTestProduct();
        when(productRepository.save(productToSave)).thenReturn(savedProduct);

        Product result = productService.saveProduct(productToSave);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(productRepository, times(1)).save(productToSave);
    }

    @Test
    void updateProduct_shouldUpdateWhenProductExists() {
        Product existingProduct = createTestProduct();
        Product productDetails = createTestProduct();
        productDetails.setName("Updated Product");
        productDetails.setDescription("Updated Description");

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Optional<Product> result = productService.updateProduct(1, productDetails);

        assertTrue(result.isPresent());
        verify(productMapper, times(1)).update(existingProduct, productDetails);
        verify(productRepository, times(1)).save(existingProduct);
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void updateProduct_shouldReturnEmptyWhenProductNotExists() {
        Product productDetails = createTestProduct();
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Product> result = productService.updateProduct(999, productDetails);

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById(999);
        verify(productMapper, never()).update(any(), any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_shouldCallRepositoryDelete() {
        productService.deleteProduct(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void updateProduct_shouldOnlyUpdateAllowedFields() {
        Product existingProduct = createTestProduct();
        Product productDetails = createTestProduct();
        productDetails.setName("Updated Name");
        productDetails.setCategoryId(999); // pretend business logic restricts changing this

        when(productRepository.findById(1)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Optional<Product> result = productService.updateProduct(1, productDetails);

        assertTrue(result.isPresent());
        verify(productMapper, times(1)).update(existingProduct, productDetails);
    }
}
