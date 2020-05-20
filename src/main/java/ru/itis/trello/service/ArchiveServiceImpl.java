package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.trello.dto.CardDto;
import ru.itis.trello.entity.Card;
import ru.itis.trello.entity.Stack;
import ru.itis.trello.repository.BoardMemberRepository;
import ru.itis.trello.repository.CardRepository;
import ru.itis.trello.repository.StackRepository;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ArchiveServiceImpl implements ArchiveService {
    private CardRepository cardRepository;
    private StackRepository stackRepository;
    private BoardMemberRepository boardMemberRepository;

    @Autowired
    public ArchiveServiceImpl(CardRepository cardRepository, StackRepository stackRepository, BoardMemberRepository boardMemberRepository) {
        this.cardRepository = cardRepository;
        this.stackRepository = stackRepository;
        this.boardMemberRepository = boardMemberRepository;
    }

    @Override
    public List<CardDto> getCards(Long boardId) {
        return CardDto.from(cardRepository.getArchivedCards(boardId));
    }

    @Override
    @Transactional
    public void addCard(Long cardId, Long userId) throws NotFoundException, AccessException {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isPresent()) {
            Optional<Stack> optionalStack = stackRepository.findById(optionalCard.get().getStack().getId());
            if (optionalStack.isPresent()) {
                if (boardMemberRepository.isBoardMemberExist(optionalStack.get().getBoard().getId(), userId).isPresent()) {
                    optionalCard.get().setArchived(true);
                    cardRepository.save(optionalCard.get());
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
