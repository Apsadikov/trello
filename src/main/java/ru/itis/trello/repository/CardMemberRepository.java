package ru.itis.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.Card;
import ru.itis.trello.entity.CardMember;
import ru.itis.trello.entity.CardMemberKey;
import ru.itis.trello.entity.User;

import java.util.List;

@Repository
public interface CardMemberRepository extends JpaRepository<CardMember, CardMemberKey> {
    void deleteCardMemberByUser(User user);

    List<CardMember> getCardMemberByCard(Card card);
}
