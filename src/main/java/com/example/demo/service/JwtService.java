package com.example.demo.service;

import com.example.demo.request.AuthenticationRequest;
import com.example.demo.response.JwtResponse;

public interface JwtService {

    JwtResponse createToken(AuthenticationRequest request);

    JwtResponse refresh(String refreshToken);
}
