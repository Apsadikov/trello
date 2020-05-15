package ru.itis.trello.dto;

import lombok.*;
import ru.itis.trello.entity.Message;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MessageDto {
    private Long id;

    private String message;

    private UserDto userDto;

    private BoardDto boardDto;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .message(message.getMessage())
                .boardDto(BoardDto.from(message.getBoard()))
                .userDto(UserDto.from(message.getUser()))
                .build();
    }

    public static List<MessageDto> from(List<Message> messages) {
        return messages.stream().map(MessageDto::from).collect(Collectors.toList());
    }
}
