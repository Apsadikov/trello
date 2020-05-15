package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.trello.dto.UserDto;
import ru.itis.trello.entity.User;
import ru.itis.trello.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findUserByName(String name) {
        return UserDto.from(userRepository.findUserByNameContaining(name));
    }
}
