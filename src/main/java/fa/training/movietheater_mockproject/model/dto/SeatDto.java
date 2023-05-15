package fa.training.movietheater_mockproject.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SeatDto {
    private Long roomId;
    private String seatName;
    private Long seatType;
}
