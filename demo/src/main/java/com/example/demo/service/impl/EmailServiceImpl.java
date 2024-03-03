package com.example.demo.service.impl;

import com.example.demo.aop.annotation.Loggable;
import com.example.demo.dto.EmailDto;
import com.example.demo.dto.EmailUpdateDto;
import com.example.demo.entity.EmailEntity;
import com.example.demo.exception.AuthException;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ConflictException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.EmailMapper;
import com.example.demo.repository.EmailRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Loggable
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    public static final String EMAIL_IS_NOT_FOUND_MESSAGE = "Email %s не найден";
    public static final String NOT_ALLOWED_TO_DELETE_ERROR_MESSAGE = "Нельзя удалить все адреса электроной почты";
    public static final String EMAIL_IS_ALREADY_REGISTRATED_ERROR_MESSAGE = "Почта %s уже зарегестированна в системе";
    public static final String EMAIL_IS_NOT_FOUND_EXCEPTION = "Почта %s не найдена в системе";
    public static final String NOT_ALLOWED_TO_UPDATE_ERROR_MESSAGE = "У вас нет прав на редактирование чужой почты";

    private final UserService userService;
    private final EmailRepository emailRepository;
    private final EmailMapper emailMapper;

    @Override
    public void deleteEmail(EmailDto deleteEmailDto) {
        var userEntity = userService.getAuthenticatedUserEntity();
        var emails = userEntity.getEmails();
        if (emails.size() > 1) {
            var emailEntity = findEmailByAddress(deleteEmailDto.getEmail(), emails);
            emailRepository.delete(emailEntity);
        } else {
            throw new BadRequestException(NOT_ALLOWED_TO_DELETE_ERROR_MESSAGE);
        }
    }

    @Override
    public void createEmail(EmailDto newEmailDto) {
        var emailEntity = emailMapper.emailDtoToEmailEntity(newEmailDto);
        if (emailRepository.findByEmail(emailEntity.getEmail()).isPresent()) {
            throw new ConflictException(EMAIL_IS_ALREADY_REGISTRATED_ERROR_MESSAGE
                    .formatted(emailEntity.getEmail()));
        }
        emailEntity.setUser(userService.getAuthenticatedUserEntity());

        emailRepository.save(emailEntity);
    }

    @Override
    @Transactional
    public void updateEmail(EmailUpdateDto emailUpdateDto) {
        if (emailRepository.existsByEmail(emailUpdateDto.getNewEmail())) {
            throw new ConflictException(EMAIL_IS_ALREADY_REGISTRATED_ERROR_MESSAGE
                    .formatted(emailUpdateDto.getNewEmail()));
        }

        var emailEntity = emailRepository.findByEmail(emailUpdateDto.getOldEmail())
                .orElseThrow(() -> new NotFoundException(EMAIL_IS_NOT_FOUND_EXCEPTION
                        .formatted(emailUpdateDto.getOldEmail())));

        if (!emailEntity.getUser().equals(userService.getAuthenticatedUserEntity())) {
            throw new AuthException(NOT_ALLOWED_TO_UPDATE_ERROR_MESSAGE);
        }
        emailEntity.setEmail(emailUpdateDto.getNewEmail());

        emailRepository.save(emailEntity);
    }

    private static EmailEntity findEmailByAddress(String emailAddress, Set<EmailEntity> emails) {
        return emails.stream().filter(emailEntity ->
                        emailEntity
                                .getEmail()
                                .equals(emailAddress))
                .findAny()
                .orElseThrow(() -> new NotFoundException(EMAIL_IS_NOT_FOUND_MESSAGE.formatted(emailAddress)));
    }
}
