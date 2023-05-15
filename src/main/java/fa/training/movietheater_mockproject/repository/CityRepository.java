package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.City;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CityRepository extends PagingAndSortingRepository<City, Long>,
        JpaSpecificationExecutor<City> {
    Optional<City> findCityByCityId(Long cityId);
}
