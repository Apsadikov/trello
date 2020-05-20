package ru.itis.trello.service;

import org.springframework.stereotype.Service;
import ru.itis.trello.dto.UserDto;
import ru.itis.trello.util.exception.NotFoundException;

import java.util.List;

@Service
public interface BoardMemberService {
    void addBoardMember(Long invitedUserId, Long memberUserId, Long boardId) throws NotFoundException;

    boolean isBoardMemberExist(Long boardId, Long userId);

    List<UserDto> getBoardMembers(Long boardId);

    void deleteBoardMember(Long deletedUserId, Long memberUserId, Long boardId) throws NotFoundException;
}
