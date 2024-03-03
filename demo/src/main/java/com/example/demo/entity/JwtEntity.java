package com.example.demo.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Tokens", timeToLive = 432000)
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class JwtEntity {

    private String id;

    private String username;

    private String authenticationToken;
}
