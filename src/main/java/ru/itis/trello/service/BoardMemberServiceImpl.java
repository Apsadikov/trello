package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.trello.entity.BoardMember;
import ru.itis.trello.entity.BoardMemberKey;
import ru.itis.trello.repository.BoardMemberRepository;
import ru.itis.trello.repository.BoardRepository;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

@Service
public class BoardMemberServiceImpl implements BoardMemberService {
    private BoardRepository boardRepository;
    private BoardMemberRepository boardMemberRepository;

    @Autowired
    public BoardMemberServiceImpl(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository) {
        this.boardMemberRepository = boardMemberRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    public void addBoardMember(Long invitedUserId, Long memberUserId, Long boardId) throws AccessException, NotFoundException {
        if (boardRepository.findById(boardId).isPresent()) {
            if (boardMemberRepository.isBoardMemberExist(boardId, memberUserId).isPresent()) {
                boardMemberRepository.save(BoardMember.builder()
                        .boardMemberKey(BoardMemberKey.builder()
                                .boardId(boardId)
                                .userId(invitedUserId)
                                .build())
                        .build());
            } else {
                throw new AccessException();
            }
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public boolean isBoardMemberExist(Long boardId, Long userId) {
        return boardMemberRepository.isBoardMemberExist(boardId, userId).isPresent();
    }
}