package ru.itis.trello.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "board")
    private List<BoardMember> boardMembers;

    @OneToMany(mappedBy = "board")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Stack> stacks;
}
