package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.ProductMapper;
import com.alexsys.smartmarket.product.model.Product;
import com.alexsys.smartmarket.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public List<Product> getAllProducts() { return productRepository.findAll(); }
    public Optional<Product> getProductById(Integer id) { return productRepository.findById(id); }
    public Product saveProduct(Product product) { return productRepository.save(product); }
    public Optional<Product> updateProduct(Integer id, Product productDetails) {
        var existingProductOptional = getProductById(id);
        if (existingProductOptional.isEmpty()) {
            return Optional.empty();
        }
        var existingProduct = existingProductOptional.get();
        productMapper.update(existingProduct, productDetails);
        return Optional.ofNullable(productRepository.save(existingProduct));
    }
    public void deleteProduct(Integer id) { productRepository.deleteById(id); }
}
