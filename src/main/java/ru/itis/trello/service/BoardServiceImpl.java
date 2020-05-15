package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.trello.dto.BoardDto;
import ru.itis.trello.entity.Board;
import ru.itis.trello.entity.BoardMember;
import ru.itis.trello.entity.BoardMemberKey;
import ru.itis.trello.repository.BoardMemberRepository;
import ru.itis.trello.repository.BoardRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {
    private BoardRepository boardRepository;
    private BoardMemberRepository boardMemberRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, BoardMemberRepository boardMemberRepository) {
        this.boardRepository = boardRepository;
        this.boardMemberRepository = boardMemberRepository;
    }

    @Override
    @Transactional
    public List<BoardDto> getBoards(Long memberId) {
        return BoardDto.from(boardRepository.getAllBoardsByMemberId(memberId));
    }

    @Override
    public Optional<Board> getBoard(Long boardId) {
        return boardRepository.findById(boardId);
    }

    @Override
    @Transactional
    public void addBoard(BoardDto boardDto) {
        Board board = boardRepository.save(Board.builder()
                .title(boardDto.getTitle())
                .build());
        boardMemberRepository.save(BoardMember.builder()
                .boardMemberKey(BoardMemberKey.builder()
                        .boardId(board.getId())
                        .userId(boardDto.getUserId())
                        .build())
                .build());
    }
}