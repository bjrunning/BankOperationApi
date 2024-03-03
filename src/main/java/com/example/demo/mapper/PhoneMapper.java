package com.example.demo.mapper;

import com.example.demo.dto.PhoneDto;
import com.example.demo.entity.PhoneEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneMapper {

    PhoneEntity phoneDtoToPhoneEntity(PhoneDto phoneDto);
}
