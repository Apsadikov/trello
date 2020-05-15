package ru.itis.trello.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stack")
@Getter
@Setter
@NamedEntityGraph(
        name = "stack-card",
        attributeNodes = {
                @NamedAttributeNode("cards"),
        }
)
public class Stack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "stack")
    private List<Card> cards;
}
