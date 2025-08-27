package com.alexsys.smartmarket.product.mapper;

import org.mapstruct.*;

import com.alexsys.smartmarket.product.model.Product;

@Mapper(
    componentModel = "spring"
)
public interface ProductMapper {

    // Update ignoring nulls
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Product target, Product source);
}
