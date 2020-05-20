package ru.itis.trello.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.trello.util.validation.Title;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddStackForm {
    @Title
    private String title;
}
