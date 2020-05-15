package ru.itis.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Board;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b INNER JOIN BoardMember bm ON bm.board = b WHERE bm.user.id = :user_id ORDER BY b.id DESC")
    List<Board> getAllBoardsByMemberId(@Param("user_id") Long userId);
}
