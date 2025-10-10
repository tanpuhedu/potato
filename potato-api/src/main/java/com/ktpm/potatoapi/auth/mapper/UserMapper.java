package com.ktpm.potatoapi.auth.mapper;

import com.ktpm.potatoapi.auth.dto.SignUpRequest;
import com.ktpm.potatoapi.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    User toEntity(SignUpRequest signUpRequest);
}
