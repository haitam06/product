package com.alexsys.smartmarket.product.repository;

import com.alexsys.smartmarket.enums.ProductAttributeType;
import com.alexsys.smartmarket.product.model.ProductAttribute;
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
class ProductAttributeRepositoryTest {

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    private ProductAttribute testAttribute;

    @BeforeEach
    void setUp() {
        testAttribute = new ProductAttribute();
        testAttribute.setValue("Red");
        testAttribute.setType(ProductAttributeType.COLOR);
        testAttribute.setProductId(1);
    }

    @Test
    void save_shouldPersistProductAttribute() {
        ProductAttribute savedAttribute = productAttributeRepository.save(testAttribute);

        assertThat(savedAttribute.getId()).isNotNull();
        assertThat(savedAttribute.getValue()).isEqualTo("Red");
        assertThat(savedAttribute.getType()).isEqualTo(ProductAttributeType.COLOR);
        assertThat(savedAttribute.getProductId()).isEqualTo(1);
    }

    @Test
    void findById_shouldReturnProductAttribute() {
        ProductAttribute savedAttribute = productAttributeRepository.save(testAttribute);

        Optional<ProductAttribute> foundAttribute = productAttributeRepository.findById(savedAttribute.getId());

        assertThat(foundAttribute).isPresent();
        assertThat(foundAttribute.get().getId()).isEqualTo(savedAttribute.getId());
        assertThat(foundAttribute.get().getValue()).isEqualTo("Red");
    }

    @Test
    void findAll_shouldReturnAllProductAttributes() {
        productAttributeRepository.save(testAttribute);

        ProductAttribute anotherAttribute = new ProductAttribute();
        anotherAttribute.setValue("Large");
        anotherAttribute.setType(ProductAttributeType.SIZE);
        anotherAttribute.setProductId(1);
        productAttributeRepository.save(anotherAttribute);

        List<ProductAttribute> attributes = productAttributeRepository.findAll();

        assertThat(attributes).hasSize(2);
    }

    @Test
    void delete_shouldRemoveProductAttribute() {
        ProductAttribute savedAttribute = productAttributeRepository.save(testAttribute);

        productAttributeRepository.deleteById(savedAttribute.getId());
        Optional<ProductAttribute> deletedAttribute = productAttributeRepository.findById(savedAttribute.getId());

        assertThat(deletedAttribute).isNotPresent();
    }

    @Test
    void update_shouldModifyProductAttribute() {
        ProductAttribute savedAttribute = productAttributeRepository.save(testAttribute);

        savedAttribute.setValue("Blue");
        savedAttribute.setType(ProductAttributeType.COLOR);
        ProductAttribute updatedAttribute = productAttributeRepository.save(savedAttribute);

        assertThat(updatedAttribute.getValue()).isEqualTo("Blue");
        assertThat(updatedAttribute.getType()).isEqualTo(ProductAttributeType.COLOR);
        assertThat(updatedAttribute.getProductId()).isEqualTo(1);
        assertThat(updatedAttribute.getId()).isEqualTo(savedAttribute.getId());
    }
}
