package ru.itis.trello.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Board;
import ru.itis.trello.entity.User;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT b FROM Board b INNER JOIN BoardMember bm ON bm.board = b WHERE bm.user.id = :user_id ORDER BY b.id DESC",
            countQuery = "SELECT count(b) FROM Board b INNER JOIN BoardMember bm ON bm.board = b WHERE bm.user.id = :user_id")
    Page<Board> getAllBoardsByMemberId(@Param("user_id") Long userId, Pageable pageable);
}
