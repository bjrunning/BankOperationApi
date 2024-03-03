package com.example.demo.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhoneNumberValidatorTest {

    @Test
    @DisplayName("Тест валидации корректного номера телефона")
    void isPhoneNumberValidTest() {
        String validPhoneNumber = "+7 800 225 25 26";

        PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();

        assertTrue(phoneNumberValidator.isValid(validPhoneNumber, null));
    }

    @Test
    @DisplayName("Тест валидации некорректного номера телефона")
    void isPhoneNumberIsNotValidTest() {
        String validPhoneNumber = "8(800)225-25-26";

        PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator();

        assertFalse(phoneNumberValidator.isValid(validPhoneNumber, null));
    }
}
