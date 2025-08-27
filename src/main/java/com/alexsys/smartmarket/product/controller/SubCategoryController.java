package com.alexsys.smartmarket.product.controller;

import com.alexsys.smartmarket.product.model.SubCategory;
import com.alexsys.smartmarket.product.service.SubCategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/smartmarket/subcategories")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;
    public SubCategoryController(SubCategoryService subCategoryService) { this.subCategoryService = subCategoryService; }

    @GetMapping
    public List<SubCategory> getAllSubCategories() { return subCategoryService.getAllSubCategories(); }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable Integer id) {
        Optional<SubCategory> subCategory = subCategoryService.getSubCategoryById(id);
        return subCategory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SubCategory createSubCategory(@RequestBody SubCategory subCategory) { return subCategoryService.saveSubCategory(subCategory); }

    @PutMapping("/{id}")
    public ResponseEntity<SubCategory> updateSubCategory(@PathVariable("id") Integer id, @RequestBody SubCategory subCategoryDetails) {
        var updatedSubCategory = subCategoryService.updateSubCategory(id, subCategoryDetails);
        return updatedSubCategory.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable Integer id) {
        subCategoryService.deleteSubCategory(id);
        return ResponseEntity.noContent().build();
    }
}
