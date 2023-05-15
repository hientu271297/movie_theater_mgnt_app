package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieService extends BaseService<Movie,Long>{
    public List<Movie> getAll();

    List<Movie> getAllByDeleteFalse();

    void create(Movie movie);

    Optional<Movie> findById(Long id);

    boolean update(Movie movie);

    void delete(Movie movie);
    boolean existName(String movieName);
    Page<Movie> getAll(Specification<Movie> specification, Pageable pageable);

    Optional<Movie> getMovieByMovieId(Long id);

    List<Movie> getAllMovieByCinemaId(Long id);

    Specification<Movie> specSearch(String keyword, String filter);

}
