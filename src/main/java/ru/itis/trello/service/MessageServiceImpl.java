package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.trello.dto.MessageDto;
import ru.itis.trello.entity.Board;
import ru.itis.trello.entity.Message;
import ru.itis.trello.entity.User;
import ru.itis.trello.repository.MessageRepository;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<MessageDto> getMessages(Long boardId) {
        return MessageDto.from(messageRepository.getMessagesByBoardId(boardId));
    }

    @Override
    public void addMessage(MessageDto messageDto) {
        Message message = messageRepository.save(Message.builder()
                .message(messageDto.getMessage())
                .board(Board.builder()
                        .id(messageDto.getBoardDto().getId())
                        .build())
                .user(User.builder()
                        .id(messageDto.getUserDto().getId())
                        .build())
                .build());
        messageDto.setId(message.getId());
    }
}
