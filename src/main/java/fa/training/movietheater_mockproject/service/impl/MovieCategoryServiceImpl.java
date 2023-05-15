package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.MovieCategory;
import fa.training.movietheater_mockproject.repository.MovieCategoryRepository;
import fa.training.movietheater_mockproject.service.MovieCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MovieCategoryServiceImpl implements MovieCategoryService {
    MovieCategoryRepository movieCategoryRepository;
    @Override
    public void createMovieCategory(MovieCategory movieCategory) {
        movieCategoryRepository.save(movieCategory);
    }

    @Override
    public void deleteAllByMovie(Movie movie) {
        movieCategoryRepository.deleteMovieCategoriesByMovie(movie);
    }

    @Override
    public List<MovieCategory> getMovieCategoriesByMovie(Movie movie) {
       return movieCategoryRepository.getMovieCategoriesByMovie(movie);
    }

    @Override
    public void deleteByMovieCategoryId(Long id) {
        movieCategoryRepository.deleteById(id);
    }
}
