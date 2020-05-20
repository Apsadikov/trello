package ru.itis.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Stack;

@Repository
public interface StackRepository extends JpaRepository<Stack, Long>, AdditionalStackRepository {

}
