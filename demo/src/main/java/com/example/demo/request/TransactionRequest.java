package com.example.demo.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @DecimalMin(value = "0.01", message = "Переводимая ссума не может быть меньше 0.01")
    private BigDecimal amount;
}
