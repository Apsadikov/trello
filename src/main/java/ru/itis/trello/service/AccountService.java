package ru.itis.trello.service;

import ru.itis.trello.dto.UserDto;
import ru.itis.trello.util.exception.ConfirmationTokenInvalid;
import ru.itis.trello.util.exception.EmailIsAlreadyUse;

public interface AccountService {
    void signUp(UserDto userDto) throws EmailIsAlreadyUse;

    void confirmEmail(String token) throws ConfirmationTokenInvalid;
}
