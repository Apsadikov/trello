package ru.itis.trello.service;

import org.springframework.stereotype.Service;
import ru.itis.trello.dto.CardDto;
import ru.itis.trello.dto.FullCardDto;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

import java.util.Date;
import java.util.Optional;

@Service
public interface CardService {
    void addCard(CardDto cardDto, Long userId) throws NotFoundException, AccessException;

    void moveCard(CardDto cardDto);

    void updateDescription(String description, Long cardId, Long userId) throws AccessException, NotFoundException;

    void updateMap(Double latitude, Double longitude, Long cardId, Long userId) throws AccessException, NotFoundException;

    void addComment(String comment, Long cardId, Long userId) throws AccessException, NotFoundException;

    void updateDeadline(Date deadline, Long cardId, Long userId) throws AccessException, NotFoundException;

    Optional<FullCardDto> getCard(Long cardId, Long id) throws NotFoundException, AccessException;
}
