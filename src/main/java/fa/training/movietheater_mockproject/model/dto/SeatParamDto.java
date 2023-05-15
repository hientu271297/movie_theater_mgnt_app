package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.model.entity.TypeSeat;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SeatParamDto {
    private String seatName;
    private TypeSeat typeSeat;
}
