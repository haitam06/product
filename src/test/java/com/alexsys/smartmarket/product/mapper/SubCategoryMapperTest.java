package com.alexsys.smartmarket.product.mapper;

import com.alexsys.smartmarket.product.model.SubCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class SubCategoryMapperTest {

    @Autowired
    private SubCategoryMapper subCategoryMapper;

    private SubCategory createSourceSubCategory() {
        SubCategory subCategory = new SubCategory();
        subCategory.setName("Smartphones");
        subCategory.setDescription("All kinds of smartphones");
        subCategory.setCategoryId(1);
        return subCategory;
    }

    private SubCategory createTargetSubCategory() {
        SubCategory subCategory = new SubCategory();
        subCategory.setId(10);
        subCategory.setName("Old Name");
        subCategory.setDescription("Old Description");
        subCategory.setCategoryId(2);
        return subCategory;
    }

    @Test
    void update_shouldUpdateNonNullFields() {
        // Arrange
        SubCategory target = createTargetSubCategory();
        SubCategory source = createSourceSubCategory();

        // Act
        subCategoryMapper.update(target, source);

        // Assert - Non-null fields updated
        assertEquals(10, target.getId()); // ID unchanged
        assertEquals("Smartphones", target.getName());
        assertEquals("All kinds of smartphones", target.getDescription());
        assertEquals(1, target.getCategoryId());
    }

    @Test
    void update_shouldNotUpdateWhenSourceFieldsAreNull() {
        // Arrange
        SubCategory target = createTargetSubCategory();
        SubCategory source = new SubCategory(); // empty source

        Integer originalId = target.getId();
        String originalName = target.getName();
        String originalDescription = target.getDescription();
        Integer originalCategoryId = target.getCategoryId();

        // Act
        subCategoryMapper.update(target, source);

        // Assert - All fields remain unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalName, target.getName());
        assertEquals(originalDescription, target.getDescription());
        assertEquals(originalCategoryId, target.getCategoryId());
    }

    @Test
    void update_shouldHandlePartialUpdates() {
        // Arrange
        SubCategory target = createTargetSubCategory();
        SubCategory source = new SubCategory();
        source.setName("New Name");

        // Act
        subCategoryMapper.update(target, source);

        // Assert - Only updated fields changed
        assertEquals(10, target.getId());
        assertEquals("New Name", target.getName());
        assertEquals("Old Description", target.getDescription());
        assertEquals(2, target.getCategoryId());
    }

    @Test
    void update_shouldWorkWithEmptyTarget() {
        // Arrange
        SubCategory target = new SubCategory(); // empty target
        SubCategory source = createSourceSubCategory();

        // Act
        subCategoryMapper.update(target, source);

        // Assert - All fields copied from source
        assertNull(target.getId()); // ID should remain null
        assertEquals("Smartphones", target.getName());
        assertEquals("All kinds of smartphones", target.getDescription());
        assertEquals(1, target.getCategoryId());
    }

    @Test
    void update_shouldWorkWithEmptySource() {
        // Arrange
        SubCategory target = createTargetSubCategory();
        SubCategory source = new SubCategory(); // empty source

        Integer originalId = target.getId();
        String originalName = target.getName();
        String originalDescription = target.getDescription();
        Integer originalCategoryId = target.getCategoryId();

        // Act
        subCategoryMapper.update(target, source);

        // Assert - All fields remain unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalName, target.getName());
        assertEquals(originalDescription, target.getDescription());
        assertEquals(originalCategoryId, target.getCategoryId());
    }
}
