package com.example.demo.entity;

import com.example.demo.validation.PhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "phones")
public class PhoneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone", unique = true, nullable = false)
    @NotBlank(message = "Номер телефона не должен быть пустым")
    @PhoneNumber(message = "Не верный формат номера телефона, введите номер в формате +7 ХХХ ХХХ ХХ ХХ")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
