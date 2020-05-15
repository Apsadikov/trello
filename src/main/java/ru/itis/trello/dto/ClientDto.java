package ru.itis.trello.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;
import ru.itis.trello.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClientDto {
    private UserDto user;
    private WebSocketSession session;
}
