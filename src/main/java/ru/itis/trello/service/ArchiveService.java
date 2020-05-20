package ru.itis.trello.service;

import org.springframework.stereotype.Service;
import ru.itis.trello.dto.CardDto;
import ru.itis.trello.util.exception.AccessException;
import ru.itis.trello.util.exception.NotFoundException;

import java.util.List;

@Service
public interface ArchiveService {
    List<CardDto> getCards(Long boardId);

    void addCard(Long cardId, Long userId) throws NotFoundException, AccessException;

}

