package com.reservio.reservation_system.presentation.mapper;

import com.reservio.reservation_system.presentation.dto.auth.LoginRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

}