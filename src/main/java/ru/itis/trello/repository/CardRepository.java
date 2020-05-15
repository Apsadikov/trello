package ru.itis.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
