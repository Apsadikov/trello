package ru.itis.trello.dto;

import lombok.*;
import ru.itis.trello.entity.Card;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardDto {
    private Long id;

    private String title;

    private StackDto stack;

    private boolean isArchived;

    public static CardDto from(Card card) {
        return CardDto.builder()
                .id(card.getId())
                .isArchived(card.isArchived())
                .stack(StackDto.from(card.getStack()))
                .title(card.getTitle())
                .build();
    }

    public static List<CardDto> from(List<Card> cards) {
        return cards.stream().map(CardDto::from).collect(Collectors.toList());
    }
}
