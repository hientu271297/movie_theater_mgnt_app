package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import fa.training.movietheater_mockproject.model.entity.MovieMovieFormat;

import java.util.List;
import java.util.Optional;

public interface MovieFormatService {
    List<MovieFormat> getAllMovieFormat();

    MovieFormat findMovieFormatByName(String movieFormatName);

    List<MovieFormat> getAll();

    Optional<MovieFormat> findMovieFormatByMovieFormatName(String name);

    MovieFormat findByMovieFormatId(Long movieId);
    Optional<MovieFormat> findMovieFormatByMovieFormatId(Long id);

    Optional<MovieFormat> findById(Long id);

    List<MovieFormat> findByMovieId(Long id);
}
