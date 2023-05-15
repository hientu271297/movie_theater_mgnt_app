package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.DateType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DateTypeRepository extends PagingAndSortingRepository<DateType, Long>,
        JpaSpecificationExecutor<DateType> {
    Optional<DateType> findDateTypeByDate(LocalDate date);

    Optional<DateType> findDateTypeByDateName(String name);
}
