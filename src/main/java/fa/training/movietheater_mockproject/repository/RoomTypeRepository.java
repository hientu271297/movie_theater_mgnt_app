package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.RoomType;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RoomTypeRepository extends PagingAndSortingRepository<RoomType,Long> {
}
