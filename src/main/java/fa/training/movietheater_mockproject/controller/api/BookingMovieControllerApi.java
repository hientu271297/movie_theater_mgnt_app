package fa.training.movietheater_mockproject.controller.api;

import fa.training.movietheater_mockproject.model.dto.bookingdto.MovieCinemaDto;
import fa.training.movietheater_mockproject.model.entity.Employee;
import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import fa.training.movietheater_mockproject.model.entity.MovieMovieFormat;
import fa.training.movietheater_mockproject.security.SecurityUtil;
import fa.training.movietheater_mockproject.service.EmployeeService;
import fa.training.movietheater_mockproject.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class BookingMovieControllerApi {

    EmployeeService employeeService;
    MovieService movieService;

    @GetMapping("/api/booking-movie")
    public ResponseEntity<List<MovieCinemaDto>> getDataMovieCinemaDto() {
        String currentEmployee = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("Not found current Employee Login"));
        Employee employee = employeeService.findByEmail(currentEmployee).get();
        List<Movie> movieList = movieService.getAllMovieByCinemaId(employee.getCinema().getCinemaId());

        //Movie -> MovieMovieFormat -> MovieFormat -> MovieCinemaDto
        List<MovieCinemaDto> movieCinemaDtoList = movieList.stream()
                .map(movie -> {
                    MovieCinemaDto movieCinemaDto = new MovieCinemaDto();
                    Set<MovieFormat> movieFormatList = movie.getMovieMovieFormats().stream()
                            .map(movieMovieFormat -> movieMovieFormat.getMovieFormat())
                            .collect(Collectors.toSet());
                    movieCinemaDto.setMovieFormat(movieFormatList);
                    movieCinemaDto.setCinemaId(employee.getCinema().getCinemaId());
                    movieCinemaDto.setCinemaName(employee.getCinema().getCinemaName());
                    BeanUtils.copyProperties(movie, movieCinemaDto);
                    return movieCinemaDto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(movieCinemaDtoList);
    }
}
