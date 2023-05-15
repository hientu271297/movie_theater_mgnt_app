package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.MovieCategory;
import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import fa.training.movietheater_mockproject.model.entity.MovieMovieFormat;

import java.util.List;
import java.util.Optional;

public interface MovieMovieFormatService {
    void createMovieMovieFormat(MovieMovieFormat movieMovieFormat);
    List<MovieMovieFormat> getMovieMovieFormatsByMovie(Movie movie);

    void deleteMovieMovieFormatById(Long id);

    Optional<MovieMovieFormat> findMovieMovieFormatByMovieAndMovieFormat(Movie movie, MovieFormat movieFormat);

    Optional<MovieMovieFormat> findByMovieAndMovieFormat(Movie movie, MovieFormat movieFormat);
}
