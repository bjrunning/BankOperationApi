package com.example.demo.service;

import com.example.demo.dto.EmailDto;
import com.example.demo.dto.EmailUpdateDto;

public interface EmailService {

    void deleteEmail(EmailDto emailDto);

    void createEmail(EmailDto emailDto);

    void updateEmail(EmailUpdateDto emailUpdateDto);
}
