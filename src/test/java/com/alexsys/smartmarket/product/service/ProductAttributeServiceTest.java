package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.ProductAttributeMapper;
import com.alexsys.smartmarket.product.model.ProductAttribute;
import com.alexsys.smartmarket.product.repository.ProductAttributeRepository;
import com.alexsys.smartmarket.enums.ProductAttributeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductAttributeServiceTest {

    @Mock
    private ProductAttributeRepository productAttributeRepository;

    @Mock
    private ProductAttributeMapper productAttributeMapper;

    @InjectMocks
    private ProductAttributeService productAttributeService;

    private ProductAttribute createTestProductAttribute() {
        ProductAttribute attribute = new ProductAttribute();
        attribute.setId(1);
        attribute.setValue("Red");
        attribute.setType(ProductAttributeType.COLOR);
        attribute.setProductId(1);
        return attribute;
    }

    @BeforeEach
    void setUp() {
        // optional: reset mocks if needed
    }

    @Test
    void getAllProductAttributes_shouldReturnAllAttributes() {
        ProductAttribute attr1 = createTestProductAttribute();
        ProductAttribute attr2 = createTestProductAttribute();
        attr2.setId(2);
        attr2.setValue("Blue");

        when(productAttributeRepository.findAll()).thenReturn(Arrays.asList(attr1, attr2));

        List<ProductAttribute> attributes = productAttributeService.getAllProductAttributes();

        assertEquals(2, attributes.size());
        verify(productAttributeRepository, times(1)).findAll();
    }

    @Test
    void getProductAttributeById_shouldReturnAttributeWhenExists() {
        ProductAttribute attribute = createTestProductAttribute();
        when(productAttributeRepository.findById(1)).thenReturn(Optional.of(attribute));

        Optional<ProductAttribute> result = productAttributeService.getProductAttributeById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Red", result.get().getValue());
        verify(productAttributeRepository, times(1)).findById(1);
    }

    @Test
    void getProductAttributeById_shouldReturnEmptyWhenNotExists() {
        when(productAttributeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ProductAttribute> result = productAttributeService.getProductAttributeById(999);

        assertFalse(result.isPresent());
        verify(productAttributeRepository, times(1)).findById(999);
    }

    @Test
    void saveProductAttribute_shouldPersistAndReturnAttribute() {
        ProductAttribute attrToSave = createTestProductAttribute();
        attrToSave.setId(null);

        ProductAttribute savedAttr = createTestProductAttribute();
        when(productAttributeRepository.save(attrToSave)).thenReturn(savedAttr);

        ProductAttribute result = productAttributeService.saveProductAttribute(attrToSave);

        assertNotNull(result.getId());
        assertEquals(1, result.getId());
        verify(productAttributeRepository, times(1)).save(attrToSave);
    }

    @Test
    void updateProductAttribute_shouldUpdateWhenAttributeExists() {
        ProductAttribute existingAttr = createTestProductAttribute();
        ProductAttribute attrDetails = createTestProductAttribute();
        attrDetails.setValue("Green");

        when(productAttributeRepository.findById(1)).thenReturn(Optional.of(existingAttr));
        when(productAttributeRepository.save(existingAttr)).thenReturn(existingAttr);

        Optional<ProductAttribute> result = productAttributeService.updateProductAttribute(1, attrDetails);

        assertTrue(result.isPresent());
        verify(productAttributeMapper, times(1)).update(existingAttr, attrDetails);
        verify(productAttributeRepository, times(1)).save(existingAttr);
        verify(productAttributeRepository, times(1)).findById(1);
    }

    @Test
    void updateProductAttribute_shouldReturnEmptyWhenAttributeNotExists() {
        ProductAttribute attrDetails = createTestProductAttribute();
        when(productAttributeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ProductAttribute> result = productAttributeService.updateProductAttribute(999, attrDetails);

        assertFalse(result.isPresent());
        verify(productAttributeRepository, times(1)).findById(999);
        verify(productAttributeMapper, never()).update(any(), any());
        verify(productAttributeRepository, never()).save(any());
    }

    @Test
    void deleteProductAttribute_shouldCallRepositoryDelete() {
        productAttributeService.deleteProductAttribute(1);
        verify(productAttributeRepository, times(1)).deleteById(1);
    }

    @Test
    void updateProductAttribute_shouldOnlyUpdateAllowedFields() {
        ProductAttribute existingAttr = createTestProductAttribute();
        ProductAttribute attrDetails = createTestProductAttribute();
        attrDetails.setValue("Yellow");
        attrDetails.setProductId(999); // suppose business logic restricts changing productId

        when(productAttributeRepository.findById(1)).thenReturn(Optional.of(existingAttr));
        when(productAttributeRepository.save(existingAttr)).thenReturn(existingAttr);

        Optional<ProductAttribute> result = productAttributeService.updateProductAttribute(1, attrDetails);

        assertTrue(result.isPresent());
        verify(productAttributeMapper, times(1)).update(existingAttr, attrDetails);
    }
}
