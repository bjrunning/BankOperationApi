package com.example.demo.service;

import com.example.demo.entity.UserEntity;
import com.example.demo.request.TransactionRequest;
import com.example.demo.request.UserCreationRequest;
import com.example.demo.response.UserListResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserListResponse getAllUsers(int page, int size, String[] sort, String usernameFilter, String emailFilter,
                                 String birthdayFilter, String phoneFilter);

    void userSingUp(UserCreationRequest userCreationRequest);

    void moneyExchange(String recipientLogin, TransactionRequest transactionRequest);

    void increaseAllUsersBalance();

    UserEntity getAuthenticatedUserEntity();
}
