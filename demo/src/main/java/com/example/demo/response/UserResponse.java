package com.example.demo.response;

import com.example.demo.dto.EmailDto;
import com.example.demo.dto.PhoneDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @Positive
    private Long id;

    @NotBlank(message = "Логин пользователя не должно быть пустым")
    @Size(min = 6, max = 20, message = "Имя пользователя должно быть от 6 до 20 знаков")
    private String login;

    @NotBlank(message = "ФИО пользователя не должно быть пустым")
    private String username;

    @Past
    @NotNull(message = "Дата рождения пользователя не должно быть пустым")
    private LocalDate birthday;

    @Valid
    private Set<EmailDto> emails;

    @Valid
    private Set<PhoneDto> phones;

    @Positive
    @DecimalMin(value = "1.00", message = "Начальные накапления не должны быть пустыми")
    private BigDecimal startBalance;

    @Positive
    @DecimalMin(value = "1.00", message = "Накапления не должны быть пустыми")
    private BigDecimal currentBalance;

    @NotBlank
    private LocalDateTime creationDateTime;
}