package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.ProductAttributeMapper;
import com.alexsys.smartmarket.product.model.ProductAttribute;
import com.alexsys.smartmarket.product.repository.ProductAttributeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeMapper productAttributeMapper;
    public ProductAttributeService(ProductAttributeRepository productAttributeRepository, ProductAttributeMapper productAttributeMapper) {
        this.productAttributeRepository = productAttributeRepository;
        this.productAttributeMapper = productAttributeMapper;
    }

    public List<ProductAttribute> getAllProductAttributes() { return productAttributeRepository.findAll(); }
    public Optional<ProductAttribute> getProductAttributeById(Integer id) { return productAttributeRepository.findById(id); }
    public ProductAttribute saveProductAttribute(ProductAttribute productAttribute) { return productAttributeRepository.save(productAttribute); }

    public Optional<ProductAttribute> updateProductAttribute(Integer id, ProductAttribute productAttributeDetails) {
        var existingProductAttributeOptional = getProductAttributeById(id);
        if (existingProductAttributeOptional.isEmpty()) {
            return Optional.empty();
        }
        var existingProductAttribute = existingProductAttributeOptional.get();
        productAttributeMapper.update(existingProductAttribute, productAttributeDetails);
        return Optional.ofNullable(productAttributeRepository.save(existingProductAttribute));
    }
    public void deleteProductAttribute(Integer id) { productAttributeRepository.deleteById(id); }
}
