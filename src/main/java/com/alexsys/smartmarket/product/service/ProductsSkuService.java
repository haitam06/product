package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.ProductsSkuMapper;
import com.alexsys.smartmarket.product.model.ProductsSku;
import com.alexsys.smartmarket.product.repository.ProductsSkuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsSkuService {

    private final ProductsSkuRepository productsSkuRepository;
    private final ProductsSkuMapper productsSkuMapper;
    public ProductsSkuService(ProductsSkuRepository productsSkuRepository, ProductsSkuMapper productsSkuMapper) {
        this.productsSkuRepository = productsSkuRepository;
        this.productsSkuMapper = productsSkuMapper;
    }

    public List<ProductsSku> getAllProductsSkus() { return productsSkuRepository.findAll(); }
    public Optional<ProductsSku> getProductsSkuById(Integer id) { return productsSkuRepository.findById(id); }
    public ProductsSku saveProductsSku(ProductsSku productsSku) { return productsSkuRepository.save(productsSku); }
    public Optional<ProductsSku> updateProductsSku(Integer id, ProductsSku productsSkuDetails) {
        var existingProductsSkuOptional = getProductsSkuById(id);
        if (existingProductsSkuOptional.isEmpty()) {
            return Optional.empty();
        }
        var existingProductsSku = existingProductsSkuOptional.get();
        productsSkuMapper.update(existingProductsSku, productsSkuDetails);
        return Optional.ofNullable(productsSkuRepository.save(existingProductsSku));
    }
    public void deleteProductsSku(Integer id) { productsSkuRepository.deleteById(id); }
}
