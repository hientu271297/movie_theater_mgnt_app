package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Food;
import fa.training.movietheater_mockproject.repository.FoodRepository;
import fa.training.movietheater_mockproject.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService {
    FoodRepository foodRepository;

    @Override
    public Optional<Food> finByFoodId(Long id) {
        return foodRepository.findByFoodId(id);
    }

    @Override
    public List<Food> getAllFood() {
        return foodRepository.getAllByDeletedFalse();
    }
}
