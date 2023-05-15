package fa.training.movietheater_mockproject.model.dto;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.Schedule;
import lombok.Data;
import java.util.List;

@Data
public class MultipleSchedulesAndMovies {
    private List<Movie> movies;
    private List<Schedule> schedules;
}
