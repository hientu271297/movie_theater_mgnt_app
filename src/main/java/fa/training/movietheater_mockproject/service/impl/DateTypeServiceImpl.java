package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.DateType;
import fa.training.movietheater_mockproject.repository.DateTypeRepository;
import fa.training.movietheater_mockproject.service.DateTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DateTypeServiceImpl implements DateTypeService {
    private final DateTypeRepository dateTypeRepository;
    @Override
    public Optional<DateType> findDateTypeByDate(LocalDate date) {
        return dateTypeRepository.findDateTypeByDate(date);
    }

    @Override
    public Optional<DateType> findDateTypeByDateName(String name) {
        return dateTypeRepository.findDateTypeByDateName(name);
    }
}
