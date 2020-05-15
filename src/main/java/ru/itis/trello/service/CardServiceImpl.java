package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.trello.dto.CardDto;
import ru.itis.trello.entity.Card;
import ru.itis.trello.entity.Stack;
import ru.itis.trello.repository.BoardMemberRepository;
import ru.itis.trello.repository.CardRepository;
import ru.itis.trello.repository.StackRepository;
import ru.itis.trello.util.exception.AccessException;

import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;
    private BoardMemberRepository boardMemberRepository;
    private StackRepository stackRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, BoardMemberRepository boardMemberRepository, StackRepository stackRepository) {
        this.cardRepository = cardRepository;
        this.boardMemberRepository = boardMemberRepository;
        this.stackRepository = stackRepository;
    }

    @Override
    public void addCard(CardDto cardDto, Long userId) throws AccessException {
        Optional<Stack> optionalStack = stackRepository.findById(cardDto.getStack().getId());
        if (optionalStack.isPresent()) {
            if (boardMemberRepository.isBoardMemberExist(optionalStack.get().getBoard().getId(), userId).isPresent()) {
                Card card = cardRepository.save(Card.builder()
                        .isArchived(false)
                        .title(cardDto.getTitle())
                        .stack(Stack.builder()
                                .id(cardDto.getStack().getId())
                                .build())
                        .build());
                cardDto.setId(card.getId());
            }
        } else {
            throw new AccessException();
        }
    }

    @Override
    public void moveCard(CardDto cardDto) {
        Optional<Card> optionalCard = cardRepository.findById(cardDto.getId());
        if (optionalCard.isPresent()) {
            optionalCard.get().setStack((Stack.builder()
                    .id(cardDto.getStack().getId())
                    .build()));
            cardRepository.save(optionalCard.get());
        }
    }
}
