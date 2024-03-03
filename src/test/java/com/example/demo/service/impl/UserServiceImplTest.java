package com.example.demo.service.impl;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.BadRequestException;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.TransactionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Тест успешной работы транзакции перевода денег")
    public void testMoneyExchange() {
        UserEntity sender = new UserEntity();
        sender.setLogin("senderLogin");
        sender.setStartBalance(new BigDecimal(200));
        sender.setCurrentBalance(new BigDecimal("100"));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("senderLogin");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findUserByLogin(eq(sender.getLogin()))).thenReturn(Optional.of(sender));

        UserEntity recipient = new UserEntity();
        recipient.setLogin("recipientLogin");
        recipient.setStartBalance(new BigDecimal(100));
        recipient.setCurrentBalance(new BigDecimal("50"));
        when(userRepository.findUserByLogin(eq(recipient.getLogin()))).thenReturn(Optional.of(recipient));

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(new BigDecimal("30"));


        userService.moneyExchange("recipientLogin", transactionRequest);

        assertEquals(new BigDecimal("70"), sender.getCurrentBalance());

        assertEquals(new BigDecimal("80"), recipient.getCurrentBalance());
    }

    @Test
    @DisplayName("Тест выбрасывания ошибки, при недостатке средств на балансе")
    public void testMoneyExchange_NotEnoughBalance() {
        UserEntity sender = new UserEntity();
        sender.setStartBalance(new BigDecimal("200"));
        sender.setCurrentBalance(new BigDecimal("100"));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("senderLogin");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        UserEntity recipient = new UserEntity();
        recipient.setStartBalance(new BigDecimal("200"));
        recipient.setCurrentBalance(new BigDecimal("50"));
        when(userRepository.findUserByLogin(anyString())).thenReturn(Optional.of(recipient));

        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(new BigDecimal("200"));

        assertThrows(BadRequestException.class, () ->
                userService.moneyExchange("recipientLogin", transactionRequest));

        verify(userRepository, times(0)).save(any());
    }
}
