package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.ProductsSkuMapper;
import com.alexsys.smartmarket.product.model.ProductsSku;
import com.alexsys.smartmarket.product.repository.ProductsSkuRepository;
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
class ProductsSkuServiceTest {

    @Mock
    private ProductsSkuRepository productsSkuRepository;

    @Mock
    private ProductsSkuMapper productsSkuMapper;

    @InjectMocks
    private ProductsSkuService productsSkuService;

    private ProductsSku createTestProductsSku() {
        ProductsSku sku = new ProductsSku();
        sku.setId(1);
        sku.setProductId(1);
        sku.setSizeAttributeId(1);
        sku.setColorAttributeId(1);
        sku.setSku("SKU001");
        sku.setPrice(99.99);
        sku.setQuantity(10);
        return sku;
    }

    @BeforeEach
    void setUp() {
        // Optional setup if needed
    }

    @Test
    void getAllProductsSkus_shouldReturnAllSkus() {
        ProductsSku sku1 = createTestProductsSku();
        ProductsSku sku2 = createTestProductsSku();
        sku2.setId(2);
        sku2.setSku("SKU002");

        when(productsSkuRepository.findAll()).thenReturn(Arrays.asList(sku1, sku2));

        List<ProductsSku> skus = productsSkuService.getAllProductsSkus();

        assertEquals(2, skus.size());
        verify(productsSkuRepository, times(1)).findAll();
    }

    @Test
    void getProductsSkuById_shouldReturnSkuWhenExists() {
        ProductsSku sku = createTestProductsSku();
        when(productsSkuRepository.findById(1)).thenReturn(Optional.of(sku));

        Optional<ProductsSku> result = productsSkuService.getProductsSkuById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("SKU001", result.get().getSku());
        verify(productsSkuRepository, times(1)).findById(1);
    }

    @Test
    void getProductsSkuById_shouldReturnEmptyWhenNotExists() {
        when(productsSkuRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ProductsSku> result = productsSkuService.getProductsSkuById(999);

        assertFalse(result.isPresent());
        verify(productsSkuRepository, times(1)).findById(999);
    }

    @Test
    void saveProductsSku_shouldPersistAndReturnSku() {
        ProductsSku skuToSave = createTestProductsSku();
        skuToSave.setId(null);

        ProductsSku savedSku = createTestProductsSku();
        when(productsSkuRepository.save(skuToSave)).thenReturn(savedSku);

        ProductsSku result = productsSkuService.saveProductsSku(skuToSave);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(productsSkuRepository, times(1)).save(skuToSave);
    }

    @Test
    void updateProductsSku_shouldUpdateWhenSkuExists() {
        ProductsSku existingSku = createTestProductsSku();
        ProductsSku skuDetails = createTestProductsSku();
        skuDetails.setPrice(149.99);
        skuDetails.setQuantity(20);

        when(productsSkuRepository.findById(1)).thenReturn(Optional.of(existingSku));
        when(productsSkuRepository.save(existingSku)).thenReturn(existingSku);

        Optional<ProductsSku> result = productsSkuService.updateProductsSku(1, skuDetails);

        assertTrue(result.isPresent());
        verify(productsSkuMapper, times(1)).update(existingSku, skuDetails);
        verify(productsSkuRepository, times(1)).save(existingSku);
        verify(productsSkuRepository, times(1)).findById(1);
    }

    @Test
    void updateProductsSku_shouldReturnEmptyWhenSkuNotExists() {
        ProductsSku skuDetails = createTestProductsSku();
        when(productsSkuRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ProductsSku> result = productsSkuService.updateProductsSku(999, skuDetails);

        assertFalse(result.isPresent());
        verify(productsSkuRepository, times(1)).findById(999);
        verify(productsSkuMapper, never()).update(any(), any());
        verify(productsSkuRepository, never()).save(any());
    }

    @Test
    void deleteProductsSku_shouldCallRepositoryDelete() {
        productsSkuService.deleteProductsSku(1);
        verify(productsSkuRepository, times(1)).deleteById(1);
    }

    @Test
    void updateProductsSku_shouldOnlyUpdateAllowedFields() {
        ProductsSku existingSku = createTestProductsSku();
        ProductsSku skuDetails = createTestProductsSku();
        skuDetails.setPrice(199.99);
        skuDetails.setQuantity(50);

        when(productsSkuRepository.findById(1)).thenReturn(Optional.of(existingSku));
        when(productsSkuRepository.save(existingSku)).thenReturn(existingSku);

        Optional<ProductsSku> result = productsSkuService.updateProductsSku(1, skuDetails);

        assertTrue(result.isPresent());
        verify(productsSkuMapper, times(1)).update(existingSku, skuDetails);
    }
}
