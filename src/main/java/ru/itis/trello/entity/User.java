package ru.itis.trello.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
@Getter
@Setter
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(length = 45, nullable = false)
    private String name;

    @Column(name = "confirmation_token", length = 128, nullable = false)
    private String confirmationToken;

    @Column(name = "is_confirmed", nullable = false)
    private boolean isConfirmed;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<BoardMember> boardMembers;
}
