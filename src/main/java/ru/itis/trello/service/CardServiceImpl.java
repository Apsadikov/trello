package ru.itis.trello.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.trello.dto.*;
import ru.itis.trello.entity.*;
import ru.itis.trello.repository.*;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;
    private BoardMemberRepository boardMemberRepository;
    private StackRepository stackRepository;
    private MapRepository mapRepository;
    private CardMemberRepository cardMemberRepository;
    private CommentRepository commentRepository;
    private FileService fileService;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository,
                           BoardMemberRepository boardMemberRepository, StackRepository stackRepository, MapRepository mapRepository, CardMemberRepository cardMemberRepository, CommentRepository commentRepository, FileService fileService) {
        this.cardRepository = cardRepository;
        this.boardMemberRepository = boardMemberRepository;
        this.stackRepository = stackRepository;
        this.mapRepository = mapRepository;
        this.cardMemberRepository = cardMemberRepository;
        this.commentRepository = commentRepository;
        this.fileService = fileService;
    }

    @Override
    @Transactional
    public void addCard(CardDto cardDto, Long userId) throws NotFoundException, AccessException {
        Optional<Stack> optionalStack = stackRepository.findById(cardDto.getStack().getId());
        if (optionalStack.isPresent()) {
            if (boardMemberRepository.isBoardMemberExist(optionalStack.get().getBoard().getId(), userId).isPresent()) {
                Card card = cardRepository.save(Card.builder()
                        .title(cardDto.getTitle())
                        .stack(Stack.builder()
                                .id(cardDto.getStack().getId())
                                .build())
                        .build());
                cardDto.setId(card.getId());
            } else {
                throw new AccessException();
            }
        } else {
            throw new NotFoundException();
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

    @Transactional
    @Override
    public void updateDescription(String description, Long cardId, Long userId) throws AccessException, NotFoundException {
        Card card = getExistCard(cardId, userId);
        card.setDescription(description);
        cardRepository.save(card);
    }

    @Override
    @Transactional
    public void updateMap(Double latitude, Double longitude, Long cardId, Long userId) throws AccessException, NotFoundException {
        Card card = getExistCard(cardId, userId);
        Optional<Map> mapOptional = mapRepository.findByCard(card);
        if (mapOptional.isPresent()) {
            mapOptional.get().setLatitude(latitude);
            mapOptional.get().setLongitude(longitude);
            mapRepository.save(mapOptional.get());
        } else {
            mapRepository.save(Map.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .card(card)
                    .build());
        }
        cardRepository.save(card);
    }

    @Override
    public void addComment(String comment, Long cardId, Long userId) throws AccessException, NotFoundException {
        Card card = getExistCard(cardId, userId);
        commentRepository.save(Comment.builder()
                .card(card)
                .user(User.builder().id(userId).build())
                .message(comment)
                .build());
    }

    @Override
    @Transactional
    public void updateDeadline(Date deadline, Long cardId, Long userId) throws AccessException, NotFoundException {
        Card card = getExistCard(cardId, userId);
        card.setDeadline(deadline);
        cardRepository.save(card);
    }

    @Override
    public Optional<FullCardDto> getCard(Long cardId, Long userId) throws NotFoundException, AccessException {
        Card card = getExistCard(cardId, userId);
        FullCardDto fullCardDto = FullCardDto.from(card);
        Optional<Map> optionalMap = mapRepository.findByCard(card);
        List<CardMember> cardMembers = cardMemberRepository.getCardMemberByCard(card);
        List<FileDto> files = fileService.getFiles(cardId, userId);
        fullCardDto.setFiles(files);
        fullCardDto.setCardMembers(CardMemberDto.from(cardMembers));
        optionalMap.ifPresent(map -> fullCardDto.setMap(MapDto.from(map)));
        fullCardDto.setComments(CommentDto.from(commentRepository.findAllByCard(card)));
        return Optional.of(fullCardDto);
    }

    private Card getExistCard(Long cardId, Long userId) throws AccessException, NotFoundException {
        Optional<Card> optionalCard = cardRepository.findById(cardId);
        if (optionalCard.isPresent()) {
            Optional<Stack> optionalStack = stackRepository.findById(optionalCard.get().getStack().getId());
            if (optionalStack.isPresent()) {
                if (boardMemberRepository.isBoardMemberExist(optionalStack.get().getBoard().getId(), userId).isPresent()) {
                    return optionalCard.get();
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
