package ru.itis.trello.service;

import org.springframework.stereotype.Service;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

@Service
public interface BoardMemberService {
    void addBoardMember(Long invitedUserId, Long memberUserId, Long boardId) throws AccessException, NotFoundException;

    boolean isBoardMemberExist(Long boardId, Long userId);
}
