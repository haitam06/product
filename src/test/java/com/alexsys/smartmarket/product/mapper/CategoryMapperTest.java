package com.alexsys.smartmarket.product.mapper;

import com.alexsys.smartmarket.product.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;

    private Category createSourceCategory() {
        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("All electronic devices");
        return category;
    }

    private Category createTargetCategory() {
        Category category = new Category();
        category.setId(1);
        category.setName("Old Name");
        category.setDescription("Old Description");
        return category;
    }

    @Test
    void update_shouldUpdateNonNullFields() {
        // Arrange
        Category target = createTargetCategory();
        Category source = createSourceCategory();

        // Act
        categoryMapper.update(target, source);

        // Assert - Non-null fields updated
        assertEquals(1, target.getId()); // ID unchanged
        assertEquals("Electronics", target.getName());
        assertEquals("All electronic devices", target.getDescription());
    }

    @Test
    void update_shouldNotUpdateWhenSourceFieldsAreNull() {
        // Arrange
        Category target = createTargetCategory();
        Category source = new Category(); // empty source

        Integer originalId = target.getId();
        String originalName = target.getName();
        String originalDescription = target.getDescription();

        // Act
        categoryMapper.update(target, source);

        // Assert - All fields remain unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalName, target.getName());
        assertEquals(originalDescription, target.getDescription());
    }

    @Test
    void update_shouldHandlePartialUpdates() {
        // Arrange
        Category target = createTargetCategory();
        Category source = new Category();
        source.setName("New Electronics");

        // Act
        categoryMapper.update(target, source);

        // Assert - Only updated fields changed
        assertEquals(1, target.getId());
        assertEquals("New Electronics", target.getName());
        assertEquals("Old Description", target.getDescription());
    }

    @Test
    void update_shouldWorkWithEmptyTarget() {
        // Arrange
        Category target = new Category(); // empty target
        Category source = createSourceCategory();

        // Act
        categoryMapper.update(target, source);

        // Assert - All fields copied from source
        assertNull(target.getId()); // ID should remain null
        assertEquals("Electronics", target.getName());
        assertEquals("All electronic devices", target.getDescription());
    }

    @Test
    void update_shouldWorkWithEmptySource() {
        // Arrange
        Category target = createTargetCategory();
        Category source = new Category(); // empty source

        Integer originalId = target.getId();
        String originalName = target.getName();
        String originalDescription = target.getDescription();

        // Act
        categoryMapper.update(target, source);

        // Assert - All fields remain unchanged
        assertEquals(originalId, target.getId());
        assertEquals(originalName, target.getName());
        assertEquals(originalDescription, target.getDescription());
    }
}
