package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDetailDto {

    private Long roomId;

    private String roomName;

    private Integer seatQuantity;

    private Boolean status;

    private String cinemaName;

    private String roomTypeName;

}
