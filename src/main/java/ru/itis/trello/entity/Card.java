package ru.itis.trello.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "card")
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 45)
    private String title;

    @ManyToOne
    @JoinColumn(name = "stack_id")
    private Stack stack;

    @Column(name = "is_archived")
    private boolean isArchived;

    @Column(name = "deadline")
    private Date deadline;

    @Column(name = "description", length = 1024)
    private String description;
}
