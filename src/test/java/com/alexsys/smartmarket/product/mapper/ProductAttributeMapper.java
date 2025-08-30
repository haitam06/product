package com.alexsys.smartmarket.product.mapper;

import com.alexsys.smartmarket.enums.ProductAttributeType;
import com.alexsys.smartmarket.product.model.ProductAttribute;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ProductAttributeMapperTest {

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    private ProductAttribute createSourceAttribute() {
        ProductAttribute attribute = new ProductAttribute();
        attribute.setValue("Red");
        attribute.setType(ProductAttributeType.COLOR);
        attribute.setProductId(1);
        return attribute;
    }

    private ProductAttribute createTargetAttribute() {
        ProductAttribute attribute = new ProductAttribute();
        attribute.setId(1);
        attribute.setValue("Blue");
        attribute.setType(ProductAttributeType.COLOR);
        attribute.setProductId(2);
        return attribute;
    }

    @Test
    void update_shouldUpdateNonNullFields() {
        // Arrange
        ProductAttribute target = createTargetAttribute();
        ProductAttribute source = createSourceAttribute();

        // Act
        productAttributeMapper.update(target, source);

        // Assert - Non-null fields updated
        assertEquals("Red", target.getValue());
        assertEquals(ProductAttributeType.COLOR, target.getType());
        assertEquals(1, target.getProductId());

        // Assert - ID remains unchanged
        assertEquals(1, target.getId());
    }

    @Test
    void update_shouldNotUpdateWhenSourceFieldsAreNull() {
        // Arrange
        ProductAttribute target = createTargetAttribute();
        ProductAttribute source = new ProductAttribute(); // empty source

        Integer originalId = target.getId();
        String originalValue = target.getValue();
        ProductAttributeType originalType = target.getType();
        Integer originalProductId = target.getProductId();

        // Act
        productAttributeMapper.update(target, source);

        // Assert - All fields unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalValue, target.getValue());
        assertEquals(originalType, target.getType());
        assertEquals(originalProductId, target.getProductId());
    }

    @Test
    void update_shouldHandlePartialUpdates() {
        // Arrange
        ProductAttribute target = createTargetAttribute();
        ProductAttribute source = new ProductAttribute();
        source.setValue("Green"); // only updating value

        // Act
        productAttributeMapper.update(target, source);

        // Assert - Only value updated
        assertEquals("Green", target.getValue());
        assertEquals(ProductAttributeType.COLOR, target.getType()); // unchanged
        assertEquals(2, target.getProductId()); // unchanged
        assertEquals(1, target.getId());
    }

    @Test
    void update_shouldWorkWithEmptyTarget() {
        // Arrange
        ProductAttribute target = new ProductAttribute(); // empty target
        ProductAttribute source = createSourceAttribute();

        // Act
        productAttributeMapper.update(target, source);

        // Assert - All fields from source copied
        assertEquals("Red", target.getValue());
        assertEquals(ProductAttributeType.COLOR, target.getType());
        assertEquals(1, target.getProductId());

        // Assert - ID remains null
        assertNull(target.getId());
    }

    @Test
    void update_shouldWorkWithEmptySource() {
        // Arrange
        ProductAttribute target = createTargetAttribute();
        ProductAttribute source = new ProductAttribute(); // empty source

        Integer originalId = target.getId();
        String originalValue = target.getValue();
        ProductAttributeType originalType = target.getType();
        Integer originalProductId = target.getProductId();

        // Act
        productAttributeMapper.update(target, source);

        // Assert - All fields remain unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalValue, target.getValue());
        assertEquals(originalType, target.getType());
        assertEquals(originalProductId, target.getProductId());
    }
}
