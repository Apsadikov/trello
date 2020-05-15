package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.trello.dto.StackDto;
import ru.itis.trello.entity.Board;
import ru.itis.trello.entity.Stack;
import ru.itis.trello.repository.BoardMemberRepository;
import ru.itis.trello.repository.StackRepository;
import ru.itis.trello.util.exception.AccessException;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

@Service
public class StackServiceImpl implements StackService {
    private StackRepository stackRepository;
    private BoardMemberRepository boardMemberRepository;
    private EntityManager entityManager;

    @Autowired
    public StackServiceImpl(StackRepository stackRepository, BoardMemberRepository boardMemberRepository, EntityManager entityManager) {
        this.stackRepository = stackRepository;
        this.boardMemberRepository = boardMemberRepository;
        this.entityManager = entityManager;
    }

    @Override
    public void addStack(StackDto stackDto, Long userId, Long boardId) throws AccessException {
        if (boardMemberRepository.isBoardMemberExist(boardId, userId).isPresent()) {
            Stack stack = stackRepository.save(Stack.builder()
                    .title(stackDto.getTitle())
                    .board(Board.builder()
                            .id(boardId)
                            .build())
                    .build());
            stackDto.setId(stack.getId());
        } else {
            throw new AccessException();
        }
    }

    @Override
    public List<Stack> getStacks(Long boardId) {
        EntityGraph entityGraph = entityManager.getEntityGraph("stack-card");
        return entityManager.createQuery("select s from Stack s LEFT JOIN Card c ON s.id = c.stack " +
                "WHERE c.isArchived = false AND s.board.id = :board_id ORDER BY s.id DESC", Stack.class)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .setParameter("board_id", boardId)
                .getResultList();
    }
}
