package com.example.demo.mapper;

import com.example.demo.entity.UserEntity;
import com.example.demo.request.UserCreationRequest;
import com.example.demo.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "authorities", ignore = true)
    UserEntity userCreationRequestToUserEntity(UserCreationRequest userCreationRequest);

    UserResponse userEntityToUserDto(UserEntity userEntity);
}
