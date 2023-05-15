package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Movie;
import fa.training.movietheater_mockproject.repository.MovieRepository;
import fa.training.movietheater_mockproject.service.MovieService;
import fa.training.movietheater_mockproject.util.ConvertUtil;
import fa.training.movietheater_mockproject.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    MovieRepository movieRepository;
    @Override
    public List<Movie> getAll() {
       return (List<Movie>) movieRepository.findAll();
    }

    @Override
    public List<Movie> getAllByDeleteFalse() {
        return movieRepository.getAllByDeletedFalse();
    }

    @Override
    public void create(Movie movie) {
        movie.setDeleted(false);
        movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieRepository.findByMovieIdAndDeletedFalse(id);
    }

    @Override
    public boolean update(Movie movie) {
        movieRepository.save(movie);
        return true;
    }

    @Override
    public void delete(Movie movie) {
        movie.setDeleted(true);
        movieRepository.save(movie);
    }

    @Override
    public boolean existName(String movieName) {
        return movieRepository.existsByMovieName(movieName);
    }

    @Override
    public Page<Movie> getAll(Specification<Movie> specification, Pageable pageable) {
        return movieRepository.findAll(specification,pageable);
    }

    @Override
    public Optional<Movie> getMovieByMovieId(Long id) {
        return movieRepository.getMovieByMovieId(id);
    }

    @Override
    public List<Movie> getAllMovieByCinemaId(Long id) {
        return  movieRepository.findAllByCinemaId(id);
    }

    @Override
    public Specification<Movie> specSearch(String keyword, String filter) {
        Specification<Movie> specUndelete = unDeleted();
        Specification<Movie> spec = null;
        if(!keyword.isEmpty() && !Objects.equals(filter,"none")){
            switch (filter){
                case "movieName":
                    spec = ((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("movieName"),"%"+keyword+"%"));
                    break;
                case "year":
                    Integer year = ConvertUtil.stringToInteger(keyword);
                    if(year != null){
                        spec = ((root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get("year"),year));
                    }
                    break;
                case "duration":
                    Integer duration = ConvertUtil.stringToInteger(keyword);
                    if(duration != null){
                        spec = ((root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get("duration"),duration));
                    }
                    break;
                case "earlyDate":
                    LocalDate earlyDate = DateUtils.stringToDate(keyword);
                    if(earlyDate != null){
                        spec = ((root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get("earlyDate"),earlyDate));
                    }
                    break;
                case "startDate":
                    LocalDate startDate = DateUtils.stringToDate(keyword);
                    if(startDate != null){
                        spec = ((root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get("startDate"),startDate));
                    }
                    break;
                case "endDate":
                    LocalDate endDate = DateUtils.stringToDate(keyword);
                    if(endDate != null){
                        spec = ((root, query, criteriaBuilder) ->
                                criteriaBuilder.equal(root.get("endDate"),endDate));
                    }
                    break;
                case "producer":
                    spec = ((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("producer"),"%"+keyword+"%"));
                    break;
                case "actor":
                    spec = ((root, query, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("actor"),"%"+keyword+"%"));
                    break;
                case "moviePrice":
                    Double price = ConvertUtil.stringToDouble(keyword);
                    if(price != null){
                        spec = ((root, query, criteriaBuilder) ->
                                criteriaBuilder.like(root.get("moviePrice"),keyword));
                    }
                    break;
            }
            if(spec != null) specUndelete = specUndelete.and(spec);
        } else if (!keyword.isEmpty() && Objects.equals(filter,"none")) {
            Specification<Movie> specOr = null;
            Double aDouble = ConvertUtil.stringToDouble(keyword);
            Integer aInteger = ConvertUtil.stringToInteger(keyword);
            LocalDate date = DateUtils.stringToDate(keyword);

            if(aDouble != null){
                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("year"),aInteger));
                specOr = spec.or(specOr);

                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("duration"),aInteger));
                specOr = spec.or(specOr);
            }

            if(aDouble != null){
                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("moviePrice"),aDouble));
                specOr = spec.or(specOr);
            }
            if(date != null){
                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("earlyDate"),date));
                specOr = spec.or(specOr);

                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("startDate"),date));
                specOr = spec.or(specOr);

                spec = ((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("earlyDate"),date));
                specOr = spec.or(specOr);
            }

            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("actor"),"%"+keyword+"%"));
            specOr = spec.or(specOr);

            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("movieName"),"%"+keyword+"%"));
            specOr = spec.or(specOr);

            spec = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("producer"),"%"+keyword+"%"));
            specOr = spec.or(specOr);

            specUndelete = specUndelete.and(specOr);

        }
        return specUndelete;
    }
}
