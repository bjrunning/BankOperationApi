package com.example.demo.response;

import lombok.*;


@Getter
@Setter
public class JwtResponse {

    public JwtResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    private String accessToken;
    private String refreshToken;
    private String type = "Bearer ";
}
