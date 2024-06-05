package com.koi151.msproperties.dto;

import com.koi151.msproperties.entity.DirectionEnum;
import com.koi151.msproperties.entity.StatusEnum;
import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullPropertyDTO {
    private String title;
    private Integer categoryId;
    private Float area;
    private Float price;
    private String description;
    private Integer totalFloor;
    private DirectionEnum houseDirection;
    private DirectionEnum balconyDirection;
    private StatusEnum status;
    private String availableFrom;
    private List<String> imageUrls;
    List<RoomDTO> rooms;
}
