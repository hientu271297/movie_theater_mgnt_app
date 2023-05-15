package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.MovieCategory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieCategoryRepository extends PagingAndSortingRepository<MovieCategory, Long> {
    void deleteMovieCategoriesByMovie(Movie movie);

    void deleteByMovieCategoryId(Long id);

    List<MovieCategory> getMovieCategoriesByMovie(Movie movie);
}
