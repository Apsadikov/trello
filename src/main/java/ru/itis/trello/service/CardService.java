package ru.itis.trello.service;

import org.springframework.stereotype.Service;
import ru.itis.trello.dto.CardDto;
import ru.itis.trello.util.exception.AccessException;

@Service
public interface CardService {
    void addCard(CardDto cardDto, Long userId) throws AccessException;

    void moveCard(CardDto cardDto);
}
