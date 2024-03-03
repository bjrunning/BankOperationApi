package com.example.demo.repository;

import com.example.demo.entity.JwtEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtRepository extends CrudRepository<JwtEntity, String> {

    Optional<JwtEntity> findByUsername(String username);
}
