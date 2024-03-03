package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserByLogin(String login);

    @Query("SELECT u FROM UserEntity u " +
            "LEFT JOIN u.emails e " +
            "LEFT JOIN u.phones p " +
            "WHERE " +
            "(:username IS NULL OR u.username LIKE :username%) AND " +
            "(:email IS NULL OR e.email = :email) AND " +
            "(:phone IS NULL OR p.phone = :phone) AND " +
            "(:birthday IS NULL OR u.birthday > :birthday)")
    Page<UserEntity> findUsersWithFilter(@Param("username") String fullName,
                                         @Param("phone") String phoneNumber,
                                         @Param("email") String email,
                                         @Param("birthday") LocalDate birthDate,
                                         Pageable pageable);

    List<UserEntity> findUsersByIsLimitAchievedFalse();
}
