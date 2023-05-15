package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import fa.training.movietheater_mockproject.model.entity.MovieMovieFormat;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieMovieFormatRepository extends PagingAndSortingRepository<MovieMovieFormat, Long> {
    List<MovieMovieFormat> getMovieMovieFormatsByMovie(Movie movie);


    Optional<MovieMovieFormat> findMovieMovieFormatByMovieAndMovieFormat(Movie movie, MovieFormat movieFormat);

    Optional<MovieMovieFormat> findByMovieMovieFormatId(Long id);

    Optional<MovieMovieFormat> findByMovieAndMovieFormat(Movie movie, MovieFormat movieFormat);
}
