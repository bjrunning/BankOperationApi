package com.example.demo.controller;

import com.example.demo.dto.EmailDto;
import com.example.demo.dto.EmailUpdateDto;
import com.example.demo.service.EmailService;
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
@RequestMapping("api/private/emails")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "Endpoint создания новой записи почты")
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
            @ApiResponse(responseCode = "409", description = "Email уже зарегестрирован",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @PostMapping
    public ResponseEntity<String> saveNewUserEmail(@RequestBody @Valid EmailDto emailDto) {
        emailService.createEmail(emailDto);

        return ResponseEntity.status(HttpStatus.CREATED).body("Почта сохранена");
    }

    @Operation(summary = "Endpoint изменения записи почты")
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
            @ApiResponse(responseCode = "404", description = "Авторизованый пользователь или email не найден в системе",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "409", description = "Email уже зарегестрирован",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @PatchMapping
    public ResponseEntity<String> transaction(@RequestBody @Valid EmailUpdateDto emailUpdateDto) {
        emailService.updateEmail(emailUpdateDto);

        return ResponseEntity.ok("Почтовый адрес изменен успешно");
    }

    @Operation(summary = "Endpoint удаления записи почты")
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
            @ApiResponse(responseCode = "404", description = "Авторизованый пользователь или email не найден в системе",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @DeleteMapping
    public ResponseEntity<String> deleteUserEmail(@RequestBody @Valid EmailDto emailDto) {
        emailService.deleteEmail(emailDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Почта удалена");
    }
}
