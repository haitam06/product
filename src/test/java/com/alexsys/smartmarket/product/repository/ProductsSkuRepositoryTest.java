package com.alexsys.smartmarket.product.repository;

import com.alexsys.smartmarket.product.model.ProductsSku;
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
class ProductsSkuRepositoryTest {

    @Autowired
    private ProductsSkuRepository productsSkuRepository;

    private ProductsSku testSku;

    @BeforeEach
    void setUp() {
        testSku = new ProductsSku();
        testSku.setProductId(1);
        testSku.setSizeAttributeId(10);
        testSku.setColorAttributeId(20);
        testSku.setSku("SKU-001");
        testSku.setPrice(199.99);
        testSku.setQuantity(50);
    }

    @Test
    void save_shouldPersistProductsSku() {
        ProductsSku savedSku = productsSkuRepository.save(testSku);

        assertThat(savedSku.getId()).isNotNull();
        assertThat(savedSku.getProductId()).isEqualTo(1);
        assertThat(savedSku.getSizeAttributeId()).isEqualTo(10);
        assertThat(savedSku.getColorAttributeId()).isEqualTo(20);
        assertThat(savedSku.getSku()).isEqualTo("SKU-001");
        assertThat(savedSku.getPrice()).isEqualTo(199.99);
        assertThat(savedSku.getQuantity()).isEqualTo(50);
    }

    @Test
    void findById_shouldReturnProductsSku() {
        ProductsSku savedSku = productsSkuRepository.save(testSku);

        Optional<ProductsSku> foundSku = productsSkuRepository.findById(savedSku.getId());

        assertThat(foundSku).isPresent();
        assertThat(foundSku.get().getId()).isEqualTo(savedSku.getId());
        assertThat(foundSku.get().getSku()).isEqualTo("SKU-001");
    }

    @Test
    void findAll_shouldReturnAllProductsSkus() {
        productsSkuRepository.save(testSku);

        ProductsSku anotherSku = new ProductsSku();
        anotherSku.setProductId(2);
        anotherSku.setSizeAttributeId(11);
        anotherSku.setColorAttributeId(21);
        anotherSku.setSku("SKU-002");
        anotherSku.setPrice(299.99);
        anotherSku.setQuantity(30);
        productsSkuRepository.save(anotherSku);

        List<ProductsSku> skus = productsSkuRepository.findAll();

        assertThat(skus).hasSize(2);
    }

    @Test
    void delete_shouldRemoveProductsSku() {
        ProductsSku savedSku = productsSkuRepository.save(testSku);

        productsSkuRepository.deleteById(savedSku.getId());
        Optional<ProductsSku> deletedSku = productsSkuRepository.findById(savedSku.getId());

        assertThat(deletedSku).isNotPresent();
    }

    @Test
    void update_shouldModifyProductsSku() {
        ProductsSku savedSku = productsSkuRepository.save(testSku);

        savedSku.setProductId(3);
        savedSku.setSizeAttributeId(12);
        savedSku.setColorAttributeId(22);
        savedSku.setSku("SKU-003");
        savedSku.setPrice(399.99);
        savedSku.setQuantity(100);

        ProductsSku updatedSku = productsSkuRepository.save(savedSku);

        assertThat(updatedSku.getProductId()).isEqualTo(3);
        assertThat(updatedSku.getSizeAttributeId()).isEqualTo(12);
        assertThat(updatedSku.getColorAttributeId()).isEqualTo(22);
        assertThat(updatedSku.getSku()).isEqualTo("SKU-003");
        assertThat(updatedSku.getPrice()).isEqualTo(399.99);
        assertThat(updatedSku.getQuantity()).isEqualTo(100);
        assertThat(updatedSku.getId()).isEqualTo(savedSku.getId());
    }
}
