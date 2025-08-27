package com.alexsys.smartmarket.product.controller;

import com.alexsys.smartmarket.product.model.ProductsSku;
import com.alexsys.smartmarket.product.service.ProductsSkuService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/smartmarket/product-skus")
public class ProductsSkuController {

    private final ProductsSkuService productSkuService;

    public ProductsSkuController(ProductsSkuService productSkuService) {
        this.productSkuService = productSkuService;
    }

    //  Get all SKUs
    @GetMapping
    public List<ProductsSku> getAllProductSkus() {
        return productSkuService.getAllProductsSkus();
    }

    //  Get SKU by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductsSku> getProductSkuById(@PathVariable Integer id) {
        Optional<ProductsSku> sku = productSkuService.getProductsSkuById(id);
        return sku.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //  Create SKU
    @PostMapping
    public ProductsSku createProductSku(@RequestBody ProductsSku productSku) {
        return productSkuService.saveProductsSku(productSku);
    }

    //  Update SKU
    @PutMapping("/{id}")
    public ResponseEntity<ProductsSku> updateProductsSku(@PathVariable("id") Integer id, @RequestBody ProductsSku productsSkuDetails) {
        var updatedProductsSku = productSkuService.updateProductsSku(id, productsSkuDetails);
        return updatedProductsSku.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    //  Delete SKU
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductSku(@PathVariable Integer id) {
        productSkuService.deleteProductsSku(id);
        return ResponseEntity.noContent().build();
    }
}
