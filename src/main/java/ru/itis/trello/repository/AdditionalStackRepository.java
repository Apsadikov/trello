package ru.itis.trello.repository;

import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Stack;

import java.util.List;

@Repository
public interface AdditionalStackRepository {
    List<Stack> getStacksByBoardId(Long boardId);
}
