package com.alexsys.smartmarket.product.repository;

import com.alexsys.smartmarket.product.model.Category;
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
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setName("Electronics");
        testCategory.setDescription("Electronic gadgets and devices");
    }

    @Test
    void save_shouldPersistCategory() {
        Category savedCategory = categoryRepository.save(testCategory);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Electronics");
        assertThat(savedCategory.getDescription()).isEqualTo("Electronic gadgets and devices");
    }

    @Test
    void findById_shouldReturnCategory() {
        Category savedCategory = categoryRepository.save(testCategory);

        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getId()).isEqualTo(savedCategory.getId());
        assertThat(foundCategory.get().getName()).isEqualTo("Electronics");
    }

    @Test
    void findAll_shouldReturnAllCategories() {
        categoryRepository.save(testCategory);

        Category anotherCategory = new Category();
        anotherCategory.setName("Books");
        anotherCategory.setDescription("All kinds of books");
        categoryRepository.save(anotherCategory);

        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).hasSize(2);
    }

    @Test
    void delete_shouldRemoveCategory() {
        Category savedCategory = categoryRepository.save(testCategory);

        categoryRepository.deleteById(savedCategory.getId());
        Optional<Category> deletedCategory = categoryRepository.findById(savedCategory.getId());

        assertThat(deletedCategory).isNotPresent();
    }

    @Test
    void update_shouldModifyCategory() {
        Category savedCategory = categoryRepository.save(testCategory);

        savedCategory.setName("Home Electronics");
        savedCategory.setDescription("Updated description");
        Category updatedCategory = categoryRepository.save(savedCategory);

        assertThat(updatedCategory.getName()).isEqualTo("Home Electronics");
        assertThat(updatedCategory.getDescription()).isEqualTo("Updated description");
        assertThat(updatedCategory.getId()).isEqualTo(savedCategory.getId());
    }
}
