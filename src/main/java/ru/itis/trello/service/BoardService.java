package ru.itis.trello.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.itis.trello.dto.BoardDto;
import ru.itis.trello.entity.Board;

import java.util.Optional;

@Service
public interface BoardService {
    Page<BoardDto> getBoards(Long memberId, Integer page);

    Optional<Board> getBoard(Long boardId);

    void addBoard(BoardDto boardDto);
}
