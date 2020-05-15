package ru.itis.trello.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MailDto {
    private String to;
    private String subject;
    private String template;
    private HashMap<String, Object> map;
}
