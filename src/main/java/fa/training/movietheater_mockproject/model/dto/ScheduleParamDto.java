package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.Room;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ScheduleParamDto {
    private Movie movie;

    private Room room;

    private LocalDate date;

    private LocalTime startAt;


}
