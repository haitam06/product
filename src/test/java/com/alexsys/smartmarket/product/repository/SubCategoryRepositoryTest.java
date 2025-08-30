package com.alexsys.smartmarket.product.repository;

import com.alexsys.smartmarket.product.model.SubCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class SubCategoryRepositoryTest {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    private SubCategory testSubCategory;

    @BeforeEach
    void setUp() {
        testSubCategory = new SubCategory();
        testSubCategory.setName("Smartphones");
        testSubCategory.setDescription("All kinds of smartphones");
        testSubCategory.setCategoryId(1); // link to Category
    }

    @Test
    void save_shouldPersistSubCategory() {
        SubCategory savedSubCategory = subCategoryRepository.save(testSubCategory);

        assertThat(savedSubCategory.getId()).isNotNull();
        assertThat(savedSubCategory.getName()).isEqualTo("Smartphones");
        assertThat(savedSubCategory.getDescription()).isEqualTo("All kinds of smartphones");
        assertThat(savedSubCategory.getCategoryId()).isEqualTo(1);
    }

    @Test
    void findById_shouldReturnSubCategory() {
        SubCategory savedSubCategory = subCategoryRepository.save(testSubCategory);

        Optional<SubCategory> foundSubCategory = subCategoryRepository.findById(savedSubCategory.getId());

        assertThat(foundSubCategory).isPresent();
        assertThat(foundSubCategory.get().getId()).isEqualTo(savedSubCategory.getId());
        assertThat(foundSubCategory.get().getName()).isEqualTo("Smartphones");
    }

    @Test
    void findAll_shouldReturnAllSubCategories() {
        subCategoryRepository.save(testSubCategory);

        SubCategory anotherSubCategory = new SubCategory();
        anotherSubCategory.setName("Laptops");
        anotherSubCategory.setDescription("All kinds of laptops");
        anotherSubCategory.setCategoryId(1);
        subCategoryRepository.save(anotherSubCategory);

        List<SubCategory> subCategories = subCategoryRepository.findAll();

        assertThat(subCategories).hasSize(2);
    }

    @Test
    void delete_shouldRemoveSubCategory() {
        SubCategory savedSubCategory = subCategoryRepository.save(testSubCategory);

        subCategoryRepository.deleteById(savedSubCategory.getId());
        Optional<SubCategory> deletedSubCategory = subCategoryRepository.findById(savedSubCategory.getId());

        assertThat(deletedSubCategory).isNotPresent();
    }

    @Test
    void update_shouldModifySubCategory() {
        SubCategory savedSubCategory = subCategoryRepository.save(testSubCategory);

        savedSubCategory.setName("High-end Smartphones");
        savedSubCategory.setDescription("Updated description");
        SubCategory updatedSubCategory = subCategoryRepository.save(savedSubCategory);

        assertThat(updatedSubCategory.getName()).isEqualTo("High-end Smartphones");
        assertThat(updatedSubCategory.getDescription()).isEqualTo("Updated description");
        assertThat(updatedSubCategory.getCategoryId()).isEqualTo(1);
        assertThat(updatedSubCategory.getId()).isEqualTo(savedSubCategory.getId());
    }
}
