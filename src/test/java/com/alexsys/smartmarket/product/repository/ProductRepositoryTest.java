package com.alexsys.smartmarket.product.repository;

import com.alexsys.smartmarket.product.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("product-test") // use a dedicated test profile for ProductRepository
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product1;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll(); // clean slate

        product1 = new Product();
        product1.setName("Product One");
        product1.setDescription("Description One");
        product1.setSummary("Summary One");
        product1.setCover("cover1.jpg");
        product1.setCategoryId(1);
    }

    @Test
    void save_shouldPersistProduct() {
        Product saved = productRepository.save(product1);
        assertNotNull(saved.getId());
        assertEquals("Product One", saved.getName());
        assertEquals(1, saved.getCategoryId());
    }

    @Test
    void findAll_shouldReturnAllProducts() {
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Product Two");
        product2.setDescription("Description Two");
        product2.setSummary("Summary Two");
        product2.setCover("cover2.jpg");
        product2.setCategoryId(2);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();
        assertEquals(2, products.size());
    }

    @Test
    void findById_shouldReturnProduct() {
        Product saved = productRepository.save(product1);
        Optional<Product> found = productRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getName(), found.get().getName());
    }

    @Test
    void update_shouldModifyProduct() {
        Product saved = productRepository.save(product1);
        saved.setName("Updated Product");
        saved.setDescription("Updated Description");

        Product updated = productRepository.save(saved);

        assertEquals("Updated Product", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
        assertEquals(saved.getId(), updated.getId());
    }

    @Test
    void delete_shouldRemoveProduct() {
        Product saved = productRepository.save(product1);
        productRepository.deleteById(saved.getId());

        Optional<Product> deleted = productRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void save_shouldHandleNullValues() {
        product1.setDescription(null);
        product1.setSummary(null);

        Product saved = productRepository.save(product1);

        assertNotNull(saved.getId());
        assertNull(saved.getDescription());
        assertNull(saved.getSummary());
    }
}
