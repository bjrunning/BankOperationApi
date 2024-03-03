package com.example.demo.dto;

import com.example.demo.validation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDto {

    @NotBlank(message = "Номер телефона не должен быть пустым")
    @PhoneNumber(message = "Не верный формат номера телефона, введите номер в формате +7 ХХХ ХХХ ХХ ХХ")
    private String phone;
}
