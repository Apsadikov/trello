package ru.itis.trello.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Stack;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class StackRepositoryImpl implements AdditionalStackRepository {
    private EntityManager entityManager;

    @Autowired
    public StackRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Stack> getStacksByBoardId(Long boardId) {
        EntityGraph entityGraph = entityManager.getEntityGraph("stack-card");
        return entityManager.createQuery("SELECT s FROM Stack s WHERE s.board.id = :board_id ORDER BY s.id DESC", Stack.class)
                .setHint("javax.persistence.fetchgraph", entityGraph)
                .setParameter("board_id", boardId)
                .getResultList();
    }
}
