package ru.itis.trello.dto;

import lombok.*;
import ru.itis.trello.entity.Stack;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StackDto {
    private Long id;
    private String title;

    public static StackDto from(Stack stack) {
        return StackDto.builder()
                .title(stack.getTitle())
                .id(stack.getId())
                .build();
    }

    public static List<StackDto> from(List<Stack> stacks) {
        return stacks.stream().map(StackDto::from).collect(Collectors.toList());
    }
}
