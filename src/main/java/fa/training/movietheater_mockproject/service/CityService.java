package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.City;

import java.util.List;
import java.util.Optional;

public interface CityService {
    List<City> getAll();

    Optional<City> findById(Long id);
}
