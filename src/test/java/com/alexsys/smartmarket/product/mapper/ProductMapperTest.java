package com.alexsys.smartmarket.product.mapper;

import com.alexsys.smartmarket.product.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    private Product createSourceProduct() {
        Product product = new Product();
        product.setName("iPhone 14");
        product.setDescription("Latest Apple smartphone");
        product.setSummary("High-end smartphone with A15 chip");
        product.setCover("iphone14.png");
        product.setCategoryId(1);
        return product;
    }

    private Product createTargetProduct() {
        Product product = new Product();
        product.setId(1);
        product.setName("Galaxy S22");
        product.setDescription("Samsung flagship phone");
        product.setSummary("Top-tier Android device");
        product.setCover("galaxy_s22.png");
        product.setCategoryId(2);
        return product;
    }

    @Test
    void update_shouldUpdateNonNullFields() {
        // Arrange
        Product target = createTargetProduct();
        Product source = createSourceProduct();

        // Act
        productMapper.update(target, source);

        // Assert - Non-null fields should be updated
        assertEquals("iPhone 14", target.getName());
        assertEquals("Latest Apple smartphone", target.getDescription());
        assertEquals("High-end smartphone with A15 chip", target.getSummary());
        assertEquals("iphone14.png", target.getCover());
        assertEquals(1, target.getCategoryId());

        // Assert - ID should remain unchanged
        assertEquals(1, target.getId());
    }

    @Test
    void update_shouldNotUpdateWhenSourceFieldsAreNull() {
        // Arrange
        Product target = createTargetProduct();
        Product source = new Product(); // Empty source

        Integer originalId = target.getId();
        String originalName = target.getName();
        String originalDescription = target.getDescription();
        String originalSummary = target.getSummary();
        String originalCover = target.getCover();
        Integer originalCategoryId = target.getCategoryId();

        // Act
        productMapper.update(target, source);

        // Assert - All fields should remain unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalName, target.getName());
        assertEquals(originalDescription, target.getDescription());
        assertEquals(originalSummary, target.getSummary());
        assertEquals(originalCover, target.getCover());
        assertEquals(originalCategoryId, target.getCategoryId());
    }

    @Test
    void update_shouldHandlePartialUpdates() {
        // Arrange
        Product target = createTargetProduct();
        Product source = new Product();
        source.setName("Pixel 8");
        source.setCategoryId(3);

        // Act
        productMapper.update(target, source);

        // Assert - Only fields set in source are updated
        assertEquals("Pixel 8", target.getName());
        assertEquals(3, target.getCategoryId());

        // Assert - Other fields remain unchanged
        assertEquals("Samsung flagship phone", target.getDescription());
        assertEquals("Top-tier Android device", target.getSummary());
        assertEquals("galaxy_s22.png", target.getCover());
        assertEquals(1, target.getId());
    }

    @Test
    void update_shouldWorkWithEmptyTarget() {
        // Arrange
        Product target = new Product(); // Empty target
        Product source = createSourceProduct();

        // Act
        productMapper.update(target, source);

        // Assert - All fields should be copied from source
        assertEquals("iPhone 14", target.getName());
        assertEquals("Latest Apple smartphone", target.getDescription());
        assertEquals("High-end smartphone with A15 chip", target.getSummary());
        assertEquals("iphone14.png", target.getCover());
        assertEquals(1, target.getCategoryId());

        // Assert - ID should remain null
        assertNull(target.getId());
    }

    @Test
    void update_shouldWorkWithEmptySource() {
        // Arrange
        Product target = createTargetProduct();
        Product source = new Product(); // Empty source

        Integer originalId = target.getId();
        String originalName = target.getName();
        String originalDescription = target.getDescription();
        String originalSummary = target.getSummary();
        String originalCover = target.getCover();
        Integer originalCategoryId = target.getCategoryId();

        // Act
        productMapper.update(target, source);

        // Assert - All fields remain unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalName, target.getName());
        assertEquals(originalDescription, target.getDescription());
        assertEquals(originalSummary, target.getSummary());
        assertEquals(originalCover, target.getCover());
        assertEquals(originalCategoryId, target.getCategoryId());
    }
}
