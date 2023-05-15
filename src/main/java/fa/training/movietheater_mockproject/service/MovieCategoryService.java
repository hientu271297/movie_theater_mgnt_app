package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.MovieCategory;

import java.util.List;

public interface MovieCategoryService {
    void createMovieCategory(MovieCategory movieCategory);
    void deleteAllByMovie(Movie movie);
    void deleteByMovieCategoryId(Long id);
    List<MovieCategory> getMovieCategoriesByMovie(Movie movie);
}
