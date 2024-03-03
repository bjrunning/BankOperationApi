package com.example.demo.controller;

import com.example.demo.request.TransactionRequest;
import com.example.demo.response.UserListResponse;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/private/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    private final UserService userService;

    @Operation(summary = "Endpoint получения списка пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserListResponse.class)
                    )}),
            @ApiResponse(responseCode = "400", description = "Неверный формат данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "403", description = "Нет прав на просмотр данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "404", description = "Авторизованый пользователь не найден в системе",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers(@Parameter(description = "Параметр фильтации по ФИО")
                                                        @RequestParam(required = false) String usernameFilter,
                                                        @Parameter(description = "Параметр фильтрации по телефону")
                                                        @RequestParam(required = false) String phoneFilter,
                                                        @Parameter(description = "Параметр фильтрации по дате рождения")
                                                        @RequestParam(required = false) String birthdayFilter,
                                                        @Parameter(description = "Параметр фильтрации по почтовому адресу")
                                                        @RequestParam(required = false) String emailFilter,
                                                        @Parameter(description = "Номер страницы")
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @Parameter(description = "Количество элементов страницы")
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @Parameter(description = "Параметр сортировки и ее порядок (убывание или возрастание)")
                                                        @RequestParam(defaultValue = "id,desc") String[] sort) {
        var usersPage = userService.getAllUsers(page, size, sort, usernameFilter, emailFilter, birthdayFilter, phoneFilter);

        return ResponseEntity.ok(usersPage);
    }

    @Operation(summary = "Endpoint перевода денег на счет другого пользователя")
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
            @ApiResponse(responseCode = "404", description = "Авторизованый пользователь не найден в системе",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @PostMapping("/transaction/{recipientLogin}")
    public ResponseEntity<String> transaction(@PathVariable("recipientLogin") String recipientLogin, //todo swagger
                                              @RequestBody @Valid TransactionRequest transactionRequest) {
        userService.moneyExchange(recipientLogin, transactionRequest);
        return ResponseEntity.ok("Перевод выполнен");
    }
}
