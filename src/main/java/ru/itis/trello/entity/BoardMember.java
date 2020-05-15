package ru.itis.trello.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "board_member")
public class BoardMember {
    @EmbeddedId
    private BoardMemberKey boardMemberKey;

    @ManyToOne
    @MapsId("user_id")
    private User user;

    @ManyToOne
    @MapsId("board_id")
    private Board board;
}
