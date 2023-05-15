package fa.training.movietheater_mockproject.repository;

import fa.training.movietheater_mockproject.model.entity.Food;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends CrudRepository<Food, Long> {
    Optional<Food> findByFoodId(Long id);

    List<Food> getAllByDeletedFalse();
}
