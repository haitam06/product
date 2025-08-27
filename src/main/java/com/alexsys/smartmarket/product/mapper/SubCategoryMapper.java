package com.alexsys.smartmarket.product.mapper;

import org.mapstruct.*;

import com.alexsys.smartmarket.product.model.SubCategory;

@Mapper(
    componentModel = "spring"
)
public interface SubCategoryMapper {

    // Update sans écraser les nulls
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget SubCategory target, SubCategory source);
}