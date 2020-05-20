package ru.itis.trello.dto;

import lombok.*;
import ru.itis.trello.entity.CardMember;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardMemberDto {

    private Long userId;

    private Long cardId;

    public static CardMemberDto from(CardMember cardMember) {
        return CardMemberDto.builder().cardId(cardMember.getCard().getId())
                .userId(cardMember.getUser().getId()).build();
    }

    public static List<CardMemberDto> from(List<CardMember> cardMembers) {
        return cardMembers.stream().map(CardMemberDto::from).collect(Collectors.toList());
    }
}
