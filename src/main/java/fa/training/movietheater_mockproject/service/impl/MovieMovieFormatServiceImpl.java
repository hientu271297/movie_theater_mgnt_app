package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.MovieCategory;
import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import fa.training.movietheater_mockproject.model.entity.MovieMovieFormat;
import fa.training.movietheater_mockproject.repository.MovieMovieFormatRepository;
import fa.training.movietheater_mockproject.service.MovieMovieFormatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieMovieFormatServiceImpl implements MovieMovieFormatService {
    private final MovieMovieFormatRepository movieMovieFormatRepository;

    @Override
    public void createMovieMovieFormat(MovieMovieFormat movieMovieFormat) {
        movieMovieFormatRepository.save(movieMovieFormat);
    }

    @Override
    public List<MovieMovieFormat> getMovieMovieFormatsByMovie(Movie movie) {
        return movieMovieFormatRepository.getMovieMovieFormatsByMovie(movie);
    }

    @Override
    public void deleteMovieMovieFormatById(Long id) {
        movieMovieFormatRepository.deleteById(id);
    }

    @Override
    public Optional<MovieMovieFormat> findMovieMovieFormatByMovieAndMovieFormat(Movie movie, MovieFormat movieFormat) {
        return movieMovieFormatRepository.findMovieMovieFormatByMovieAndMovieFormat(movie, movieFormat);
    }

    @Override
    public Optional<MovieMovieFormat> findByMovieAndMovieFormat(Movie movie, MovieFormat movieFormat) {
        return movieMovieFormatRepository.findByMovieAndMovieFormat(movie, movieFormat);
    }
}
