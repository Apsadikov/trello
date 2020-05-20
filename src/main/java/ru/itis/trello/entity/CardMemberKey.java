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
public class CardMemberKey implements Serializable {
    @Column(name = "card_id")
    private Long cardId;
    @Column(name = "user_id")
    private Long userId;
}