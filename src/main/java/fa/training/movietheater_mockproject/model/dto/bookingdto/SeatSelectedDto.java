package fa.training.movietheater_mockproject.model.dto.bookingdto;

import fa.training.movietheater_mockproject.enums.Select;
import lombok.Data;

@Data
public class SeatSelectedDto {

    private Long seatId;

    private Long roomId;

    private String roomName;

    private String seatName;
    private Long seatType;

    private Select status;

    private Integer numberHorizontalRowSeats;

    private Integer numberHorizontalColumnSeats;
}
