package ru.itis.trello.service;

import org.springframework.stereotype.Controller;
import ru.itis.trello.dto.MessageDto;

import java.util.List;

@Controller
public interface MessageService {
    List<MessageDto> getMessages(Long boardId);

    void addMessage(MessageDto messageDto);
}
