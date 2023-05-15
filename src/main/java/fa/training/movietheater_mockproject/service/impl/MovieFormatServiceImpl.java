package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.MovieFormat;
import fa.training.movietheater_mockproject.repository.MovieFormatRepository;
import fa.training.movietheater_mockproject.service.MovieFormatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MovieFormatServiceImpl implements MovieFormatService {

    private final MovieFormatRepository movieFormatRepository;
    @Override
    public List<MovieFormat> getAllMovieFormat() {
        return (List<MovieFormat>) movieFormatRepository.findAll();
    }

    @Override
    public MovieFormat findMovieFormatByName(String movieFormatName) {
        return movieFormatRepository.findByMovieFormatName(movieFormatName).get();
    }

    @Override
    public List<MovieFormat> getAll() {
        return (List<MovieFormat>) movieFormatRepository.findAll();
    }

    @Override
    public Optional<MovieFormat> findMovieFormatByMovieFormatName(String name) {
        return movieFormatRepository.findMovieFormatByMovieFormatName(name);
    }

    @Override
    public MovieFormat findByMovieFormatId(Long movieFormatId) {
        return movieFormatRepository.findByMovieFormatId(movieFormatId).orElseThrow(()-> new RuntimeException("Not Found Movie Format Id")) ;
    }

    @Override
    public Optional<MovieFormat> findMovieFormatByMovieFormatId(Long id) {
        return movieFormatRepository.findMovieFormatByMovieFormatId(id);
    }

    @Override
    public Optional<MovieFormat> findById(Long id) {
        return movieFormatRepository.findById(id);
    }


    @Override
    public List<MovieFormat> findByMovieId(Long id) {
        return movieFormatRepository.findByMovieId(id);
    }
}
