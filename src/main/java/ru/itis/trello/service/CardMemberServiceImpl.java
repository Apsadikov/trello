package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itis.trello.entity.Card;
import ru.itis.trello.entity.CardMember;
import ru.itis.trello.entity.CardMemberKey;
import ru.itis.trello.entity.Stack;
import ru.itis.trello.repository.CardMemberRepository;
import ru.itis.trello.repository.CardRepository;
import ru.itis.trello.repository.StackRepository;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

import java.util.Optional;

@Service
public class CardMemberServiceImpl implements CardMemberService {
    private StackRepository stackRepository;
    private CardMemberRepository cardMemberRepository;
    private CardRepository cardRepository;
    private BoardMemberService boardMemberService;

    @Autowired
    public CardMemberServiceImpl(StackRepository stackRepository, CardMemberRepository cardMemberRepository,
                                 CardRepository cardRepository, BoardMemberService boardMemberService) {
        this.stackRepository = stackRepository;
        this.cardMemberRepository = cardMemberRepository;
        this.cardRepository = cardRepository;
        this.boardMemberService = boardMemberService;
    }

    @Override
    public void addCardMember(Long invitedUserId, Long memberUserId, Long cardId) throws NotFoundException, AccessException {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isPresent()) {
            Optional<Stack> optionalStack = stackRepository.findById(optionalCard.get().getStack().getId());
            if (optionalStack.isPresent()) {
                if (boardMemberService.isBoardMemberExist(optionalStack.get().getBoard().getId(), memberUserId)) {
                    cardMemberRepository.save(CardMember.builder()
                            .cardMemberKey(CardMemberKey.builder()
                                    .cardId(cardId)
                                    .userId(invitedUserId)
                                    .build())
                            .build());
                } else {
                    throw new AccessException();
                }
            } else {
                throw new NotFoundException();
            }
        } else {
            throw new NotFoundException();
        }
    }
}
