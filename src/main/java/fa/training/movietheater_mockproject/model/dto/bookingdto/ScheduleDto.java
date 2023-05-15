package fa.training.movietheater_mockproject.model.dto.bookingdto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ScheduleDto {

    @NotNull(message = "Invalid ScheduleId!!!")
    private Long scheduleId;
    @NotNull(message = "Please chose start time!!!")
    private LocalTime startAt;
    @NotNull(message = "Please chose start date")
    private LocalDate date;
}
