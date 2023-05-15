package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.model.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleWrapper {

    private Long roomId;
    private String roomName;
    private Long cityId;
    private Long cinemaId;
    private String cityName;
    private String cinemaName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<ScheduleDto> scheduleDtos;

}
