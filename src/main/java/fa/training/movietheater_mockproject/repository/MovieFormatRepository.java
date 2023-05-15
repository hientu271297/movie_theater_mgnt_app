package fa.training.movietheater_mockproject.repository;


import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieFormatRepository extends PagingAndSortingRepository<MovieFormat, Long> {
    Optional<MovieFormat> findByMovieFormatId(Long movieFormatId);
    Optional<MovieFormat> findByMovieFormatName(String movieFormatName);

    Optional<MovieFormat> findMovieFormatByMovieFormatName(String name);

    Optional<MovieFormat> findMovieFormatByMovieFormatId(Long id);

    @Query(value = "SELECT m FROM MovieFormat m " +
            " JOIN m.movieMovieFormats mm" +
            " WHERE mm.movie.movieId = ?1 ")
    List<MovieFormat> findByMovieId(Long id);

}
