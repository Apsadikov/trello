package ru.itis.trello.security.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.trello.entity.User;
import ru.itis.trello.repository.UserRepository;

import java.util.Optional;

@Service(value = "customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionUser = userRepository.findUserByEmail(email);
        if (optionUser.isPresent()) {
            return new UserDetailsImpl(optionUser.get());
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
