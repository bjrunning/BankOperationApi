package com.example.demo.controller;

import com.example.demo.request.AuthenticationRequest;
import com.example.demo.request.JwtRefreshRequest;
import com.example.demo.request.UserCreationRequest;
import com.example.demo.response.JwtResponse;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
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
@RequestMapping("api/public/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;

    private final JwtService jwtService;

    @Operation(summary = "Endpoint регистрации пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная операция",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "409", description = "Неверный формат данных",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "409", description = "Пользователь уже зарегестрирован",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @PostMapping
    ResponseEntity<String> regUser(@RequestBody @Valid UserCreationRequest userCreationRequest) {
        userService.userSingUp(userCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Пользователь создан");
    }

    @Operation(
            summary = "Контроллер генерации токенов",
            description = "Генерирует access и refresh токены для зарегестрированного пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class)
                    )}),
            @ApiResponse(responseCode = "403", description = "Неверные логин или пароль",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @PostMapping("/singIn")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody AuthenticationRequest request) {
        JwtResponse jwtResponse = jwtService.createToken(request);
        return ResponseEntity.ok().body(jwtResponse);
    }

    @Operation(
            summary = "Контроллер обновления токенов",
            description = "Обнавляет access и refresh токены"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class)
                    )}),
            @ApiResponse(responseCode = "403", description = "Jwt refresh token просрочен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )}),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema
                    )})
    })
    @PostMapping({"/refresh"})
    public ResponseEntity<JwtResponse> refresh(@RequestBody JwtRefreshRequest refresh) {
        JwtResponse token = jwtService.refresh(refresh.getRefreshToken());
        return ResponseEntity.ok().body(token);
    }
}
