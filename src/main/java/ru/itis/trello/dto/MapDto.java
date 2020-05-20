package ru.itis.trello.dto;

import lombok.*;
import ru.itis.trello.entity.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MapDto {
    private Double latitude;

    private Double longitude;

    public static MapDto from(Map map) {
        if (map == null) {
            return null;
        }
        return MapDto.builder()
                .longitude(map.getLongitude())
                .latitude(map.getLatitude())
                .build();
    }
}
