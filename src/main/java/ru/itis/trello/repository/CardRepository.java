package ru.itis.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Card;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Card c INNER JOIN Stack s ON c.stack = s INNER JOIN Board b ON s.board = b WHERE b.id = :board_id AND c.isArchived = true")
    List<Card> getArchivedCards(@Param("board_id") Long boardId);
}
