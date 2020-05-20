package ru.itis.trello.service;

import org.springframework.stereotype.Service;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

@Service
public interface CardMemberService {
    void addCardMember(Long invitedUserId, Long memberUserId, Long cardId) throws NotFoundException, AccessException;
}
