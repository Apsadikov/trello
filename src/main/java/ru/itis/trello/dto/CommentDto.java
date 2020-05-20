package ru.itis.trello.dto;

import lombok.*;
import ru.itis.trello.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;

    private String message;
    private Long userId;

    public static CommentDto from(Comment comment) {
        return CommentDto.builder().userId(comment.getUser().getId()).id(comment.getId()).message(comment.getMessage()).build();
    }

    public static List<CommentDto> from(List<Comment> comments) {
        return comments.stream().map(CommentDto::from).collect(Collectors.toList());
    }

}
