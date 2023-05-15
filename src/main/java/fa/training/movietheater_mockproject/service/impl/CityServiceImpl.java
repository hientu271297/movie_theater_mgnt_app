package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.City;
import fa.training.movietheater_mockproject.repository.CityRepository;
import fa.training.movietheater_mockproject.service.CityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public List<City> getAll() {
        return (List<City>) cityRepository.findAll();
    }
    public Optional<City> findById(Long id) {
        return cityRepository.findById(id);
    }


}
