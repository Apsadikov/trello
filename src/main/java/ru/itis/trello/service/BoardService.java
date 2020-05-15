package ru.itis.trello.service;

import org.springframework.stereotype.Service;
import ru.itis.trello.dto.BoardDto;
import ru.itis.trello.entity.Board;

import java.util.List;
import java.util.Optional;

@Service
public interface BoardService {
    List<BoardDto> getBoards(Long memberId);

    Optional<Board> getBoard(Long boardId);

    void addBoard(BoardDto boardDto);
}
