package fa.training.movietheater_mockproject.model.dto.bookingdto;

import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
public class MovieCinemaDto {

    @NotNull(message = "Invalid Movie id!!!")
    private Long movieId;

    @NotNull(message = "Please chose movie name!!!")
    private String movieName;

    @NotNull(message = "Invalid cinema id!!!")
    private Long cinemaId;

    @NotNull(message = "Please chose cinema name!!!")
    private String cinemaName;

    @NotNull
    private Set<MovieFormat> movieFormat;


}
