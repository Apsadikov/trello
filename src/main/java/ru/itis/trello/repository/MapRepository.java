package ru.itis.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Card;
import ru.itis.trello.entity.Map;

import java.util.Optional;

@Repository
public interface MapRepository  extends JpaRepository<Map, Long> {
    Optional<Map> findByCard(Card card);
}
