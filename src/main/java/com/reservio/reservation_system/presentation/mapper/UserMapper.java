package com.reservio.reservation_system.presentation.mapper;

import com.reservio.reservation_system.infrastructure.entity.UserEntity;
import com.reservio.reservation_system.presentation.dto.User.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "usrFirstName", target = "firstName")
    @Mapping(source = "usrLastName", target = "lastName")
    @Mapping(source = "usrEmail", target = "email")
    UserDto userToUserDto(UserEntity user);

}
