package ru.itis.trello.websocket.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message {
    private String text;
    private Long roomId;
    private String name;
}
