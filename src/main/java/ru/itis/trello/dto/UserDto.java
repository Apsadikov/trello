package ru.itis.trello.dto;

import lombok.*;
import ru.itis.trello.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String confirmationToken;

    private boolean isConfirmed;

    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .isConfirmed(user.isConfirmed())
                .build();
    }

    public static List<UserDto> from(List<User> users) {
        return users.stream().map(UserDto::from).collect(Collectors.toList());
    }
}
