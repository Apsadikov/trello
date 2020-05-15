package ru.itis.trello.service;

import org.springframework.stereotype.Service;
import ru.itis.trello.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findUserByName(String name);
}
