package com.example.demo.mapper;

import com.example.demo.dto.EmailDto;
import com.example.demo.entity.EmailEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    EmailEntity emailDtoToEmailEntity(EmailDto emailDto);
}
