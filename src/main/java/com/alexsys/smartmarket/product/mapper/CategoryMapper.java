package com.alexsys.smartmarket.product.mapper;

import org.mapstruct.*;

import com.alexsys.smartmarket.product.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    // Update existing category (ignore null values)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Category target, Category source);
}
