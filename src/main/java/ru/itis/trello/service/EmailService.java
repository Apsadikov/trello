package ru.itis.trello.service;

import ru.itis.trello.dto.MailDto;

public interface EmailService {
    void send(MailDto mail);
}
