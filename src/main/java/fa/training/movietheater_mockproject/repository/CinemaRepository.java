package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CinemaRepository extends PagingAndSortingRepository<Cinema, Long>,
        JpaSpecificationExecutor<Cinema> {

    List<Cinema> getCinemasByCity(City city);
    List<Cinema> findByCityEquals(City city);
}
