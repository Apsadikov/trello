package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.trello.dto.UserDto;
import ru.itis.trello.entity.BoardMember;
import ru.itis.trello.entity.BoardMemberKey;
import ru.itis.trello.entity.User;
import ru.itis.trello.repository.BoardMemberRepository;
import ru.itis.trello.repository.BoardRepository;
import ru.itis.trello.repository.CardMemberRepository;
import ru.itis.trello.util.exception.NotFoundException;

import java.util.List;

@Service
public class BoardMemberServiceImpl implements BoardMemberService {
    private BoardRepository boardRepository;
    private BoardMemberRepository boardMemberRepository;
    private CardMemberRepository cardMemberRepository;

    @Autowired
    public BoardMemberServiceImpl(BoardMemberRepository boardMemberRepository, BoardRepository boardRepository, CardMemberRepository cardMemberRepository) {
        this.boardMemberRepository = boardMemberRepository;
        this.boardRepository = boardRepository;
        this.cardMemberRepository = cardMemberRepository;
    }

    @Override
    public void addBoardMember(Long invitedUserId, Long memberUserId, Long boardId) throws NotFoundException {
        if (boardRepository.findById(boardId).isPresent()) {
            boardMemberRepository.save(BoardMember.builder()
                    .boardMemberKey(BoardMemberKey.builder()
                            .boardId(boardId)
                            .userId(invitedUserId)
                            .build())
                    .build());
        } else {
            throw new NotFoundException();
        }
    }

    @Override
    public boolean isBoardMemberExist(Long boardId, Long userId) {
        return boardMemberRepository.isBoardMemberExist(boardId, userId).isPresent();
    }

    @Override
    public List<UserDto> getBoardMembers(Long boardId) {
        return UserDto.from(boardMemberRepository.getMembersByBoardId(boardId));
    }

    @Override
    @Transactional
    public void deleteBoardMember(Long deletedUserId, Long memberUserId, Long boardId) throws NotFoundException {
        if (boardRepository.findById(boardId).isPresent()) {
            boardMemberRepository.delete(BoardMember.builder()
                    .boardMemberKey(BoardMemberKey.builder()
                            .boardId(boardId)
                            .userId(deletedUserId)
                            .build())
                    .build());
            cardMemberRepository.deleteCardMemberByUser(User.builder()
                    .id(deletedUserId)
                    .build());
        } else {
            throw new NotFoundException();
        }
    }
}