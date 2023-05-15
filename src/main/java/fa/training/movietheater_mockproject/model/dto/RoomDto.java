package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.RoomType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoomDto {
    private Long roomId;
    @NotBlank(message = "Room name is required!")
    private String roomName;
    private Boolean status;
    private RoomType roomType;
    private Cinema cinema;
}
