package com.alexsys.smartmarket.product.controller;

import com.alexsys.smartmarket.product.model.ProductAttribute;
import com.alexsys.smartmarket.product.service.ProductAttributeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/smartmarket/product-attributes")
public class ProductAttributeController {
    private final ProductAttributeService productAttributeService;
    public ProductAttributeController(ProductAttributeService productAttributeService) { this.productAttributeService = productAttributeService; }

    @GetMapping
    public List<ProductAttribute> getAllProductAttributes() { return productAttributeService.getAllProductAttributes(); }

    @GetMapping("/{id}")
    public ResponseEntity<ProductAttribute> getProductAttributeById(@PathVariable Integer id) {
        Optional<ProductAttribute> attribute = productAttributeService.getProductAttributeById(id);
        return attribute.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductAttribute createProductAttribute(@RequestBody ProductAttribute productAttribute) { return productAttributeService.saveProductAttribute(productAttribute); }

    @PutMapping("/{id}")
    public ResponseEntity<ProductAttribute> updateProductAttribute(@PathVariable("id") Integer id, @RequestBody ProductAttribute productAttributeDetails) {
        var updatedProductAttribute = productAttributeService.updateProductAttribute(id, productAttributeDetails);
        return updatedProductAttribute.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductAttribute(@PathVariable Integer id) {
        productAttributeService.deleteProductAttribute(id);
        return ResponseEntity.noContent().build();
    }
}
