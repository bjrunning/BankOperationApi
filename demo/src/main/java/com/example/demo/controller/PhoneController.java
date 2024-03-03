package com.example.demo.controller;

import com.example.demo.dto.PhoneDto;
import com.example.demo.dto.PhoneUpdateDto;
import com.example.demo.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/private/phones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class PhoneController {

    private final PhoneService phoneService;

    @Operation(summary = "Endpoint создания номера телефона")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Успешная операция",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "400", description = "Неверный формат данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "403", description = "Нет прав на изменение данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "404", description = "Авторизованый пользователь не найден в системе",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "409", description = "Телефон уже зарегестрирован",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @PostMapping
    public ResponseEntity<String> saveNewUserPhone(@RequestBody @Valid PhoneDto phone) {
        phoneService.createPhone(phone);

        return ResponseEntity.status(HttpStatus.CREATED).body("Номер телефона сохранен");
    }

    @Operation(summary = "Endpoint изменения номера телефона")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "400", description = "Неверный формат данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "403", description = "Нет прав на изменение данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "404", description = "Авторизованый пользователь или телефон не найден в системе",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "409", description = "Телефон уже зарегестрирован",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @PatchMapping
    public ResponseEntity<String> transaction(@RequestBody @Valid PhoneUpdateDto phoneUpdateDto) {
        phoneService.updatePhone(phoneUpdateDto);

        return ResponseEntity.ok("Номер телефона изменен успешно");
    }

    @Operation(summary = "Endpoint удаления номера телефона")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешная операция",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "400", description = "Неверный формат данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "403", description = "Нет прав на изменение данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "404", description = "Авторизованый пользователь или телефон не найден в системе",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
    })
    @DeleteMapping
    public ResponseEntity<String> deleteUserPhone(@RequestBody @Valid PhoneDto phone) {
        phoneService.deletePhone(phone);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Почта сохранена");
    }
}
