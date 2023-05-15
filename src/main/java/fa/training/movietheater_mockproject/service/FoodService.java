package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Food;

import java.util.List;
import java.util.Optional;

public interface FoodService  {

    Optional<Food> finByFoodId(Long id);

    List<Food> getAllFood();
}
