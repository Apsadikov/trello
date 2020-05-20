package ru.itis.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Card;
import ru.itis.trello.entity.File;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFile(String file);

    List<File> findByCard(Card card);
}
