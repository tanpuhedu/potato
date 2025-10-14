package com.ktpm.potatoapi.option.mapper;

import com.ktpm.potatoapi.option.dto.OptionCreationRequest;
import com.ktpm.potatoapi.option.dto.OptionResponse;
import com.ktpm.potatoapi.option.entity.Option;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OptionValueMapper.class)
public interface OptionMapper {
    @Mapping(target = "optionValues", ignore = true)
    Option toEntity(OptionCreationRequest dto);

    OptionResponse toResponse(Option entity);
}
