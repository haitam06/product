package com.alexsys.smartmarket.product.mapper;

import com.alexsys.smartmarket.product.model.ProductsSku;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ProductsSkuMapperTest {

    @Autowired
    private ProductsSkuMapper productsSkuMapper;

    private ProductsSku createSourceSku() {
        ProductsSku sku = new ProductsSku();
        sku.setProductId(1);
        sku.setSizeAttributeId(10);
        sku.setColorAttributeId(20);
        sku.setSku("SKU-001");
        sku.setPrice(99.99);
        sku.setQuantity(50);
        return sku;
    }

    private ProductsSku createTargetSku() {
        ProductsSku sku = new ProductsSku();
        sku.setId(1);
        sku.setProductId(2);
        sku.setSizeAttributeId(15);
        sku.setColorAttributeId(25);
        sku.setSku("SKU-OLD");
        sku.setPrice(79.99);
        sku.setQuantity(30);
        return sku;
    }

    @Test
    void update_shouldUpdateNonNullFields() {
        // Arrange
        ProductsSku target = createTargetSku();
        ProductsSku source = createSourceSku();

        // Act
        productsSkuMapper.update(target, source);

        // Assert - Non-null fields updated
        assertEquals(1, target.getId()); // ID unchanged
        assertEquals(1, target.getProductId());
        assertEquals(10, target.getSizeAttributeId());
        assertEquals(20, target.getColorAttributeId());
        assertEquals("SKU-001", target.getSku());
        assertEquals(99.99, target.getPrice());
        assertEquals(50, target.getQuantity());
    }

    @Test
    void update_shouldNotUpdateWhenSourceFieldsAreNull() {
        // Arrange
        ProductsSku target = createTargetSku();
        ProductsSku source = new ProductsSku(); // empty source

        Integer originalId = target.getId();
        Integer originalProductId = target.getProductId();
        Integer originalSize = target.getSizeAttributeId();
        Integer originalColor = target.getColorAttributeId();
        String originalSku = target.getSku();
        Double originalPrice = target.getPrice();
        Integer originalQuantity = target.getQuantity();

        // Act
        productsSkuMapper.update(target, source);

        // Assert - All fields remain unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalProductId, target.getProductId());
        assertEquals(originalSize, target.getSizeAttributeId());
        assertEquals(originalColor, target.getColorAttributeId());
        assertEquals(originalSku, target.getSku());
        assertEquals(originalPrice, target.getPrice());
        assertEquals(originalQuantity, target.getQuantity());
    }

    @Test
    void update_shouldHandlePartialUpdates() {
        // Arrange
        ProductsSku target = createTargetSku();
        ProductsSku source = new ProductsSku();
        source.setPrice(120.0);
        source.setQuantity(100);

        // Act
        productsSkuMapper.update(target, source);

        // Assert - Only updated fields changed
        assertEquals(1, target.getId());
        assertEquals(2, target.getProductId());
        assertEquals(15, target.getSizeAttributeId());
        assertEquals(25, target.getColorAttributeId());
        assertEquals("SKU-OLD", target.getSku());
        assertEquals(120.0, target.getPrice());
        assertEquals(100, target.getQuantity());
    }

    @Test
    void update_shouldWorkWithEmptyTarget() {
        // Arrange
        ProductsSku target = new ProductsSku(); // empty target
        ProductsSku source = createSourceSku();

        // Act
        productsSkuMapper.update(target, source);

        // Assert - All fields copied from source
        assertNull(target.getId());
        assertEquals(1, target.getProductId());
        assertEquals(10, target.getSizeAttributeId());
        assertEquals(20, target.getColorAttributeId());
        assertEquals("SKU-001", target.getSku());
        assertEquals(99.99, target.getPrice());
        assertEquals(50, target.getQuantity());
    }

    @Test
    void update_shouldWorkWithEmptySource() {
        // Arrange
        ProductsSku target = createTargetSku();
        ProductsSku source = new ProductsSku(); // empty source

        Integer originalId = target.getId();
        Integer originalProductId = target.getProductId();
        Integer originalSize = target.getSizeAttributeId();
        Integer originalColor = target.getColorAttributeId();
        String originalSku = target.getSku();
        Double originalPrice = target.getPrice();
        Integer originalQuantity = target.getQuantity();

        // Act
        productsSkuMapper.update(target, source);

        // Assert - All fields remain unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalProductId, target.getProductId());
        assertEquals(originalSize, target.getSizeAttributeId());
        assertEquals(originalColor, target.getColorAttributeId());
        assertEquals(originalSku, target.getSku());
        assertEquals(originalPrice, target.getPrice());
        assertEquals(originalQuantity, target.getQuantity());
    }
}
