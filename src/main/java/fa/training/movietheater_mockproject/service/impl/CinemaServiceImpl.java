package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Cinema;
import fa.training.movietheater_mockproject.model.entity.City;
import fa.training.movietheater_mockproject.repository.CinemaRepository;
import fa.training.movietheater_mockproject.service.CinemaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CinemaServiceImpl implements CinemaService {

    private final CinemaRepository cinemaRepository;

    @Override
    public List<Cinema> getAllListCinema() {
        return (List<Cinema>) cinemaRepository.findAll();
    }

    @Override
    public Cinema getCinemaById(Long id) {
        return cinemaRepository.findById(id).get();
    }
    @Override
    public List<Cinema> getCinemaByCity(City city) {
        return cinemaRepository.findByCityEquals(city);
    }

    public List<Cinema> getAll() {
        return (List<Cinema>) cinemaRepository.findAll();
    }
}
