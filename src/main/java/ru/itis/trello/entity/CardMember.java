package ru.itis.trello.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "card_member")
public class CardMember {
    @EmbeddedId
    private CardMemberKey cardMemberKey;

    @ManyToOne
    @MapsId("user_id")
    private User user;

    @ManyToOne
    @MapsId("card_id")
    private Card card;
}
