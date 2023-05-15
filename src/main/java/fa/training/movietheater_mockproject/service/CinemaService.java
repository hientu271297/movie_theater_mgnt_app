package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.City;

import java.util.List;

public interface CinemaService extends BaseService<Cinema, Long>{
    List<Cinema> getAllListCinema();
    Cinema getCinemaById(Long id);
    List<Cinema> getCinemaByCity(City city);
    List<Cinema> getAll();
}
