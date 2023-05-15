package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.TypeSeat;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TypeSeatRepository extends PagingAndSortingRepository<TypeSeat,Long> {
}
