package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.repository.custom.CustomizeBookingRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long>, CustomizeBookingRepository,
        JpaSpecificationExecutor<Movie> {

    List<Movie> getAllByDeletedFalse();
    Optional<Movie> findByMovieIdAndDeletedFalse(Long id);
    boolean existsByMovieName(String movieName);

    List<Movie> getTop3ByDeletedFalseAndHottedMovieTrueOrderByStartDate();

    Optional<Movie> getMovieByMovieId(Long id);

    List<Movie> findAllByDeletedFalse();

    @Query(value = "SELECT DISTINCT m.* FROM movie m JOIN movie_movie_format mmf ON m.movie_id = mmf.movie_id \n" +
            "JOIN schedule s ON s.movie_movie_format_id = mmf.movie_movie_format_id \n" +
            "JOIN room r ON s.room_id = r.room_id\n" +
            "WHERE r.cinema_id = ?1 AND m.deleted = 0 " ,
            nativeQuery = true)
    List<Movie> findAllByCinemaId(Long id);
}
