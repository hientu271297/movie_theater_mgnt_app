package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.DateType;

import java.time.LocalDate;
import java.util.Optional;

public interface DateTypeService {
    Optional<DateType> findDateTypeByDate(LocalDate date);

    Optional<DateType> findDateTypeByDateName(String name);
}
