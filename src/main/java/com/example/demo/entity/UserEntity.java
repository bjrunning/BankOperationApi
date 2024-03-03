package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false)
    @NotBlank(message = "Логин пользователя не должно быть пустым")
    @Size(min = 6, max = 20, message = "Имя пользователя должно быть от 6 до 20 знаков")
    private String login;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Пароль не должен быть пустым")
    @Size(min = 6, max = 50, message = "Пароль должен быть от 6 до 20 знаков")
    private String password;

    @Column(name = "username", nullable = false)
    @NotBlank(message = "ФИО пользователя не должно быть пустым")
    private String username;

    @Column(name = "birthday", nullable = false)
    @NotNull(message = "Дата рождения пользователя не должно быть пустым")
    private LocalDate birthday;

    @Valid
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<EmailEntity> emails;

    @Valid
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<PhoneEntity> phones;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "start_balance", nullable = false)
    @DecimalMin(value = "0.01", message = "Начальные накапления не должны быть пустыми")
    private BigDecimal startBalance;

    @Column(name = "current_balance", nullable = false)
    @DecimalMin(value = "0.01", message = "Текущие накапленя не могут быть отрицательными")
    private BigDecimal currentBalance;

    @Column(name = "creation_date_time", nullable = false)
    private LocalDateTime creationDateTime;

    @Column(name = "is_limit_achieved")
    private boolean isLimitAchieved;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
