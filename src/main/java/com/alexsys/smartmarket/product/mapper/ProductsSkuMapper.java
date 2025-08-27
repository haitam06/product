package com.alexsys.smartmarket.product.mapper;

import org.mapstruct.*;

import com.alexsys.smartmarket.product.model.ProductsSku;

@Mapper(
    componentModel = "spring"
)
public interface ProductsSkuMapper {

    // Update sans écraser les nulls
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget ProductsSku target, ProductsSku source);
}
