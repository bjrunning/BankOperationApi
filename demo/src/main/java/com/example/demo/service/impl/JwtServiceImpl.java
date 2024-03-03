package com.example.demo.service.impl;

import com.example.demo.aop.annotation.Loggable;
import com.example.demo.entity.JwtEntity;
import com.example.demo.exception.AuthException;
import com.example.demo.repository.JwtRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.AuthenticationRequest;
import com.example.demo.response.JwtResponse;
import com.example.demo.service.JwtService;
import com.example.demo.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Loggable
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    public static final String WRONG_CREDENTIALS_ERROR_MESSAGE = "Проверьте правильность ввода логина и пароля";
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtRepository jwtRepository;
    private final JwtUtils jwtUtils;

    public JwtResponse createToken(AuthenticationRequest request) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getLogin()
                        , request.getPassword()));
        UserDetails user = (UserDetails) authentication.getPrincipal();

        if (user != null) {

            var accessToken = jwtUtils.generateAccessToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(user);

            jwtRepository.findByUsername(user.getUsername()).ifPresent(jwtRepository::delete);

            cacheToken(request.getLogin(), refreshToken);

            return new JwtResponse(accessToken, refreshToken);
        }
        throw new AuthException(WRONG_CREDENTIALS_ERROR_MESSAGE);
    }

    private void cacheToken(String login, String refreshToken) {
        JwtEntity jwtEntity = new JwtEntity();
        jwtEntity.setAuthenticationToken(refreshToken);
        jwtEntity.setUsername(login);

        jwtRepository.save(jwtEntity);
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        var username = jwtUtils.extractRefreshTokenUsername(refreshToken);

        if (jwtUtils.validateRefreshToken(refreshToken)) {
            var savedToken = jwtRepository.findByUsername(username).orElse(null);
            var user = userRepository.findUserByLogin(username)
                    .orElseThrow(() -> new AuthException("Пользователь не найден"));
            if (savedToken != null && savedToken.getAuthenticationToken().equals(refreshToken)) {
                var accessToken = jwtUtils.generateAccessToken(user);
                var newRefreshToken = jwtUtils.generateRefreshToken(user);

                jwtRepository.delete(savedToken);
                cacheToken(username, refreshToken);

                return new JwtResponse(accessToken, newRefreshToken);
            }
        }

        throw new AuthException("Jwt токен просрочен");
    }
}
