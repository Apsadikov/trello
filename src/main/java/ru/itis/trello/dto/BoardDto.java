package ru.itis.trello.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.trello.entity.Board;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BoardDto {
    private Long id;

    private String title;

    private Long userId;

    public static BoardDto from(Board board) {
        return BoardDto.builder()
                .title(board.getTitle())
                .id(board.getId()).build();
    }

    public static List<BoardDto> from(List<Board> boards) {
        return boards.stream().map(BoardDto::from).collect(Collectors.toList());
    }
}
