package ru.itis.trello.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class BoardMemberKey implements Serializable {
    @Column(name = "board_id")
    private Long boardId;
    @Column(name = "user_id")
    private Long userId;
}