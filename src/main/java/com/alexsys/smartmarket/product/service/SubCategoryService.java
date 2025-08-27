package com.alexsys.smartmarket.product.service;

import com.alexsys.smartmarket.product.mapper.SubCategoryMapper;
import com.alexsys.smartmarket.product.model.SubCategory;
import com.alexsys.smartmarket.product.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final SubCategoryMapper subCategoryMapper;
    public SubCategoryService(SubCategoryRepository subCategoryRepository, SubCategoryMapper subCategoryMapper) {
        this.subCategoryRepository = subCategoryRepository;
        this.subCategoryMapper = subCategoryMapper;
    }

    public List<SubCategory> getAllSubCategories() { return subCategoryRepository.findAll(); }
    public Optional<SubCategory> getSubCategoryById(Integer id) { return subCategoryRepository.findById(id); }
    public SubCategory saveSubCategory(SubCategory subCategory) { return subCategoryRepository.save(subCategory); }
    public Optional<SubCategory> updateSubCategory(Integer id, SubCategory subCategoryDetails) {
        var existingSubCategoryOptional = getSubCategoryById(id);
        if (existingSubCategoryOptional.isEmpty()) {
            return Optional.empty();
        }
        var existingSubCategory = existingSubCategoryOptional.get();
        subCategoryMapper.update(existingSubCategory, subCategoryDetails);
        return Optional.ofNullable(subCategoryRepository.save(existingSubCategory));
    }
    public void deleteSubCategory(Integer id) { subCategoryRepository.deleteById(id); }
}
