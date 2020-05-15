package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.trello.dto.MailDto;
import ru.itis.trello.dto.UserDto;
import ru.itis.trello.entity.Role;
import ru.itis.trello.entity.User;
import ru.itis.trello.repository.UserRepository;
import ru.itis.trello.util.ConfirmationTokenGenerator;
import ru.itis.trello.util.exception.ConfirmationTokenInvalid;
import ru.itis.trello.util.exception.EmailIsAlreadyUse;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final EmailService emailService;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AccountServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                              @Qualifier(value = "confirmationEmailService") EmailService emailService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
    }

    @Override
    public void signUp(UserDto userDto) throws EmailIsAlreadyUse {
        if (!userRepository.findUserByEmail(userDto.getEmail()).isPresent()) {
            User user = userRepository.save(User.builder()
                    .email(userDto.getEmail())
                    .name(userDto.getName())
                    .passwordHash(bCryptPasswordEncoder.encode(userDto.getPassword()))
                    .confirmationToken(ConfirmationTokenGenerator.generate())
                    .name(userDto.getName())
                    .role(Role.USER)
                    .build());
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("token", user.getConfirmationToken());
            emailService.send(MailDto.builder()
                    .subject("Confirm email")
                    .map(parameters)
                    .template("confirmation-mail.ftlh")
                    .build());
        } else {
            throw new EmailIsAlreadyUse();
        }
    }

    @Override
    public void confirmEmail(String token) throws ConfirmationTokenInvalid {
        Optional<User> optionalUser = userRepository.findUserByConfirmationToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getConfirmationToken().equals(token)) {
                user.setConfirmed(true);
                userRepository.save(user);
            } else {
                throw new ConfirmationTokenInvalid();
            }
        }
    }
}
