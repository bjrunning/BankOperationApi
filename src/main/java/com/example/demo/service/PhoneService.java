package com.example.demo.service;

import com.example.demo.dto.PhoneDto;
import com.example.demo.dto.PhoneUpdateDto;

public interface PhoneService {

    void deletePhone(PhoneDto phoneDto);

    void createPhone(PhoneDto phoneDto);

    void updatePhone(PhoneUpdateDto phoneUpdateDto);
}