package ru.itis.trello.dto;

import lombok.*;
import ru.itis.trello.entity.File;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileDto {

    private String file;

    private java.io.File realFile;

    public static FileDto from(File file) {
        return FileDto.builder()
                .file(file.getFile())
                .build();
    }

    public static List<FileDto> from(List<File> files) {
        return files.stream().map(FileDto::from).collect(Collectors.toList());
    }
}
