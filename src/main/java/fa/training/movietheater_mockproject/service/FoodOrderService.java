package fa.training.movietheater_mockproject.service;

import fa.training.movietheater_mockproject.model.entity.Bill;
import fa.training.movietheater_mockproject.model.entity.FoodOrder;

import java.util.List;

public interface FoodOrderService {
    List<FoodOrder> getAllFoodOrderByBill(Bill bill);
}
