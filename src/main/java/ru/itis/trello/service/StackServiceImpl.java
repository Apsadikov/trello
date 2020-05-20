package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.trello.dto.StackDto;
import ru.itis.trello.entity.Board;
import ru.itis.trello.entity.Stack;
import ru.itis.trello.repository.StackRepository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StackServiceImpl implements StackService {
    private StackRepository stackRepository;
    private EntityManager entityManager;

    @Autowired
    public StackServiceImpl(StackRepository stackRepository, EntityManager entityManager) {
        this.stackRepository = stackRepository;
        this.entityManager = entityManager;
    }

    @Override
    public void addStack(StackDto stackDto, Long userId, Long boardId) {
        Stack stack = stackRepository.save(Stack.builder()
                .title(stackDto.getTitle())
                .board(Board.builder()
                        .id(boardId)
                        .build())
                .build());
        stackDto.setId(stack.getId());
    }

    @Override
    public List<StackDto> getStacks(Long boardId) {
        List<Stack> stacks = stackRepository.getStacksByBoardId(boardId);
        stacks.forEach(stack -> stack.setCards(stack.getCards().stream().filter(card -> !card.isArchived())
                .collect(Collectors.toList())));
        return StackDto.from(stacks);
    }
}
