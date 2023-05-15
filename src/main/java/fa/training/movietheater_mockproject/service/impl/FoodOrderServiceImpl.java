package fa.training.movietheater_mockproject.service.impl;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.FoodOrder;
import fa.training.movietheater_mockproject.repository.FoodOrderRepository;
import fa.training.movietheater_mockproject.service.FoodOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FoodOrderServiceImpl implements FoodOrderService {

    private final FoodOrderRepository foodOrderRepository;

    @Override
    public List<FoodOrder> getAllFoodOrderByBill(Bill bill) {
        return foodOrderRepository.findAllByBill(bill);
    }
}
