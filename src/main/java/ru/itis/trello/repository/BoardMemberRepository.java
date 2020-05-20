package ru.itis.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.BoardMember;
import ru.itis.trello.entity.BoardMemberKey;
import ru.itis.trello.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardMemberRepository extends JpaRepository<BoardMember, BoardMemberKey> {
    @Query("SELECT bm FROM BoardMember bm WHERE bm.board.id = :board_id AND bm.user.id = :user_id")
    Optional<BoardMember> isBoardMemberExist(@Param("board_id") Long boardId, @Param("user_id") Long userId);

    @Query(value = "SELECT u FROM User u INNER JOIN BoardMember bm ON bm.user = u WHERE bm.board.id = :board_id")
    List<User> getMembersByBoardId(@Param("board_id") Long boardId);
}
