package ru.itis.trello.service;

import org.springframework.stereotype.Service;
import ru.itis.trello.dto.StackDto;
import ru.itis.trello.entity.Stack;
import ru.itis.trello.util.exception.AccessException;

import java.util.List;

@Service
public interface StackService {
    void addStack(StackDto stackDto, Long userId, Long boardId);

    List<StackDto> getStacks(Long boardId);
}
